package com.thiagomf.interviewschedulerapi.service;

import com.thiagomf.interviewschedulerapi.dto.CancelInterviewRequest;
import com.thiagomf.interviewschedulerapi.dto.CreateInterviewRequest;
import com.thiagomf.interviewschedulerapi.dto.DashboardResponse;
import com.thiagomf.interviewschedulerapi.dto.InterviewResponse;
import com.thiagomf.interviewschedulerapi.entity.Interview;
import com.thiagomf.interviewschedulerapi.entity.InterviewStatus;
import com.thiagomf.interviewschedulerapi.entity.Role;
import com.thiagomf.interviewschedulerapi.entity.User;
import com.thiagomf.interviewschedulerapi.exception.InvalidInterviewStateException;
import com.thiagomf.interviewschedulerapi.exception.ResourceNotFoundException;
import com.thiagomf.interviewschedulerapi.exception.ScheduleConflictException;
import com.thiagomf.interviewschedulerapi.exception.UnauthorizedActionException;
import com.thiagomf.interviewschedulerapi.repository.InterviewRepository;
import com.thiagomf.interviewschedulerapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;

    public InterviewResponse createInterview(String recruiterEmail, CreateInterviewRequest request) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new UnauthorizedActionException("Only recruiters can schedule interviews");
        }

        User candidate = userRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new UnauthorizedActionException("Selected user is not a candidate");
        }

        if (request.getDurationMinutes() != 30 && request.getDurationMinutes() != 60) {
            throw new InvalidInterviewStateException("Interview duration must be 30 or 60 minutes");
        }

        validateScheduleConflict(recruiter, candidate, request.getScheduledAt(), request.getDurationMinutes());

        Interview interview = Interview.builder()
                .recruiter(recruiter)
                .candidate(candidate)
                .scheduledAt(request.getScheduledAt())
                .durationMinutes(request.getDurationMinutes())
                .status(InterviewStatus.SCHEDULED)
                .notes(request.getNotes())
                .build();

        return mapToResponse(interviewRepository.save(interview));
    }

    public List<InterviewResponse> getMyInterviews(String email, InterviewStatus status, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        List<Interview> recruiterInterviews;
        List<Interview> candidateInterviews;

        if (status != null) {
            recruiterInterviews = interviewRepository
                    .findByRecruiterEmailAndStatusOrderByScheduledAtAsc(email, status, pageable)
                    .getContent();

            candidateInterviews = interviewRepository
                    .findByCandidateEmailAndStatusOrderByScheduledAtAsc(email, status, pageable)
                    .getContent();
        } else {
            recruiterInterviews = interviewRepository
                    .findByRecruiterEmailOrderByScheduledAtAsc(email, pageable)
                    .getContent();

            candidateInterviews = interviewRepository
                    .findByCandidateEmailOrderByScheduledAtAsc(email, pageable)
                    .getContent();
        }

        return mergeAndMapInterviews(recruiterInterviews, candidateInterviews);
    }

    public InterviewResponse getInterviewById(Long interviewId, String email) {
        Interview interview = findInterview(interviewId);

        if (!canAccessInterview(interview, email)) {
            throw new UnauthorizedActionException("You do not have access to this interview");
        }

        return mapToResponse(interview);
    }

    public InterviewResponse cancelInterview(Long interviewId, String email, CancelInterviewRequest request) {
        Interview interview = findInterview(interviewId);

        if (!canAccessInterview(interview, email)) {
            throw new UnauthorizedActionException("You do not have permission to cancel this interview");
        }

        if (interview.getStatus() != InterviewStatus.SCHEDULED) {
            throw new InvalidInterviewStateException("Only scheduled interviews can be canceled");
        }

        interview.setStatus(InterviewStatus.CANCELED);
        interview.setCancellationReason(request.getReason());

        return mapToResponse(interviewRepository.save(interview));
    }

    public InterviewResponse completeInterview(Long interviewId, String email) {
        Interview interview = findInterview(interviewId);

        if (!interview.getRecruiter().getEmail().equals(email)) {
            throw new UnauthorizedActionException("Only the recruiter can complete this interview");
        }

        if (interview.getStatus() != InterviewStatus.SCHEDULED) {
            throw new InvalidInterviewStateException("Only scheduled interviews can be completed");
        }

        interview.setStatus(InterviewStatus.COMPLETED);

        return mapToResponse(interviewRepository.save(interview));
    }

    public DashboardResponse getDashboard(String email) {
        long totalInterviews = interviewRepository.countByRecruiterEmailOrCandidateEmail(email, email);
        long scheduledInterviews = interviewRepository.countByRecruiterEmailAndStatusOrCandidateEmailAndStatus(
                email, InterviewStatus.SCHEDULED, email, InterviewStatus.SCHEDULED
        );
        long completedInterviews = interviewRepository.countByRecruiterEmailAndStatusOrCandidateEmailAndStatus(
                email, InterviewStatus.COMPLETED, email, InterviewStatus.COMPLETED
        );
        long canceledInterviews = interviewRepository.countByRecruiterEmailAndStatusOrCandidateEmailAndStatus(
                email, InterviewStatus.CANCELED, email, InterviewStatus.CANCELED
        );

        return DashboardResponse.builder()
                .totalInterviews(totalInterviews)
                .scheduledInterviews(scheduledInterviews)
                .completedInterviews(completedInterviews)
                .canceledInterviews(canceledInterviews)
                .build();
    }

    private Interview findInterview(Long interviewId) {
        return interviewRepository.findById(interviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
    }

    private boolean canAccessInterview(Interview interview, String email) {
        return interview.getRecruiter().getEmail().equals(email)
                || interview.getCandidate().getEmail().equals(email);
    }

    private void validateScheduleConflict(User recruiter, User candidate, LocalDateTime scheduledAt, Integer durationMinutes) {
        LocalDateTime newInterviewEnd = scheduledAt.plusMinutes(durationMinutes);

        List<Interview> recruiterInterviews = interviewRepository
                .findByRecruiterEmailAndStatusOrderByScheduledAtAsc(
                        recruiter.getEmail(),
                        InterviewStatus.SCHEDULED,
                        PageRequest.of(0, 1000)
                )
                .getContent();

        List<Interview> candidateInterviews = interviewRepository
                .findByCandidateEmailAndStatusOrderByScheduledAtAsc(
                        candidate.getEmail(),
                        InterviewStatus.SCHEDULED,
                        PageRequest.of(0, 1000)
                )
                .getContent();

        for (Interview interview : recruiterInterviews) {
            LocalDateTime existingStart = interview.getScheduledAt();
            LocalDateTime existingEnd = existingStart.plusMinutes(interview.getDurationMinutes());

            if (scheduledAt.isBefore(existingEnd) && newInterviewEnd.isAfter(existingStart)) {
                throw new ScheduleConflictException("Recruiter already has an interview in this time slot");
            }
        }

        for (Interview interview : candidateInterviews) {
            LocalDateTime existingStart = interview.getScheduledAt();
            LocalDateTime existingEnd = existingStart.plusMinutes(interview.getDurationMinutes());

            if (scheduledAt.isBefore(existingEnd) && newInterviewEnd.isAfter(existingStart)) {
                throw new ScheduleConflictException("Candidate already has an interview in this time slot");
            }
        }
    }

    private List<InterviewResponse> mergeAndMapInterviews(List<Interview> recruiterInterviews, List<Interview> candidateInterviews) {
        List<Interview> allInterviews = recruiterInterviews.stream().toList();

        List<Interview> merged = new java.util.ArrayList<>(allInterviews);

        for (Interview interview : candidateInterviews) {
            boolean alreadyAdded = merged.stream().anyMatch(existing -> existing.getId().equals(interview.getId()));
            if (!alreadyAdded) {
                merged.add(interview);
            }
        }

        return merged.stream()
                .sorted((a, b) -> a.getScheduledAt().compareTo(b.getScheduledAt()))
                .map(this::mapToResponse)
                .toList();
    }

    private InterviewResponse mapToResponse(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .recruiterId(interview.getRecruiter().getId())
                .recruiterName(interview.getRecruiter().getName())
                .candidateId(interview.getCandidate().getId())
                .candidateName(interview.getCandidate().getName())
                .scheduledAt(interview.getScheduledAt())
                .durationMinutes(interview.getDurationMinutes())
                .status(interview.getStatus())
                .notes(interview.getNotes())
                .cancellationReason(interview.getCancellationReason())
                .build();
    }
}