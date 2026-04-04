package com.thiagomf.interviewschedulerapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelInterviewRequest {

    @NotBlank
    private String reason;
}