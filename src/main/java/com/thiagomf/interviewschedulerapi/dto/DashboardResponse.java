package com.thiagomf.interviewschedulerapi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardResponse {

    private long totalInterviews;
    private long scheduledInterviews;
    private long completedInterviews;
    private long canceledInterviews;
}