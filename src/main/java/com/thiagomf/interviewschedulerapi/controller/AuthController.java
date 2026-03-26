package com.thiagomf.interviewschedulerapi.controller;

import com.thiagomf.interviewschedulerapi.dto.RegisterRequest;
import com.thiagomf.interviewschedulerapi.dto.UserResponse;
import com.thiagomf.interviewschedulerapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid RegisterRequest request) {
        return userService.register(request);
    }
}