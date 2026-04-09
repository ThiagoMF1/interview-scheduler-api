package com.thiagomf.interviewschedulerapi.controller;

import com.thiagomf.interviewschedulerapi.dto.DashboardResponse;
import com.thiagomf.interviewschedulerapi.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final InterviewService interviewService;

    @GetMapping
    public DashboardResponse getDashboard(Authentication authentication) {
        return interviewService.getDashboard(authentication.getName());
    }
}