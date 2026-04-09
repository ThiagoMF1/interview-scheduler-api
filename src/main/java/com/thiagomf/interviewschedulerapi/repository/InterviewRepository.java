package com.thiagomf.interviewschedulerapi.repository;

import com.thiagomf.interviewschedulerapi.entity.Interview;
import com.thiagomf.interviewschedulerapi.entity.InterviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Page<Interview> findByRecruiterEmailOrderByScheduledAtAsc(String recruiterEmail, Pageable pageable);

    Page<Interview> findByCandidateEmailOrderByScheduledAtAsc(String candidateEmail, Pageable pageable);

    Page<Interview> findByRecruiterEmailAndStatusOrderByScheduledAtAsc(String recruiterEmail, InterviewStatus status, Pageable pageable);

    Page<Interview> findByCandidateEmailAndStatusOrderByScheduledAtAsc(String candidateEmail, InterviewStatus status, Pageable pageable);

    long countByRecruiterEmailOrCandidateEmail(String recruiterEmail, String candidateEmail);

    long countByRecruiterEmailAndStatusOrCandidateEmailAndStatus(
            String recruiterEmail,
            InterviewStatus recruiterStatus,
            String candidateEmail,
            InterviewStatus candidateStatus
    );
}