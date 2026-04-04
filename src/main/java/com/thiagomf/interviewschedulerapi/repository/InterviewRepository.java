package com.thiagomf.interviewschedulerapi.repository;

import com.thiagomf.interviewschedulerapi.entity.Interview;
import com.thiagomf.interviewschedulerapi.entity.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByRecruiterIdAndStatus(Long recruiterId, InterviewStatus status);

    List<Interview> findByCandidateIdAndStatus(Long candidateId, InterviewStatus status);

    List<Interview> findByRecruiterEmailOrderByScheduledAtAsc(String recruiterEmail);

    List<Interview> findByCandidateEmailOrderByScheduledAtAsc(String candidateEmail);
}