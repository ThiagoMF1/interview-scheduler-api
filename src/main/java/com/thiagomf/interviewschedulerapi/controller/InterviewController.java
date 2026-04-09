package com.thiagomf.interviewschedulerapi.controller;

import com.thiagomf.interviewschedulerapi.dto.CancelInterviewRequest;
import com.thiagomf.interviewschedulerapi.dto.CreateInterviewRequest;
import com.thiagomf.interviewschedulerapi.dto.InterviewResponse;
import com.thiagomf.interviewschedulerapi.entity.InterviewStatus;
import com.thiagomf.interviewschedulerapi.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public InterviewResponse createInterview(@RequestBody @Valid CreateInterviewRequest request,
                                             Authentication authentication) {
        return interviewService.createInterview(authentication.getName(), request);
    }

    @GetMapping("/me")
    public List<InterviewResponse> getMyInterviews(
            Authentication authentication,
            @RequestParam(required = false) InterviewStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return interviewService.getMyInterviews(authentication.getName(), status, page, size);
    }

    @GetMapping("/{interviewId}")
    public InterviewResponse getInterviewById(@PathVariable Long interviewId,
                                              Authentication authentication) {
        return interviewService.getInterviewById(interviewId, authentication.getName());
    }

    @PatchMapping("/{interviewId}/cancel")
    public InterviewResponse cancelInterview(@PathVariable Long interviewId,
                                             @RequestBody @Valid CancelInterviewRequest request,
                                             Authentication authentication) {
        return interviewService.cancelInterview(interviewId, authentication.getName(), request);
    }

    @PatchMapping("/{interviewId}/complete")
    public InterviewResponse completeInterview(@PathVariable Long interviewId,
                                               Authentication authentication) {
        return interviewService.completeInterview(interviewId, authentication.getName());
    }
}