package com.thiagomf.interviewschedulerapi.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateInterviewRequest {

    @NotNull
    private Long candidateId;

    @NotNull
    @Future
    private LocalDateTime scheduledAt;

    @NotNull
    @Min(30)
    @Max(60)
    private Integer durationMinutes;

    private String notes;
}