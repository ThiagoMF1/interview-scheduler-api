package com.thiagomf.interviewschedulerapi.dto;

import com.thiagomf.interviewschedulerapi.entity.InterviewStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InterviewResponse {

    private Long id;
    private Long recruiterId;
    private String recruiterName;
    private Long candidateId;
    private String candidateName;
    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private InterviewStatus status;
    private String notes;
    private String cancellationReason;
}