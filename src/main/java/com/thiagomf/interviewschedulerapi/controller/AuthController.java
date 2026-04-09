package com.thiagomf.interviewschedulerapi.controller;

import com.thiagomf.interviewschedulerapi.dto.AuthResponse;
import com.thiagomf.interviewschedulerapi.dto.LoginRequest;
import com.thiagomf.interviewschedulerapi.dto.RegisterRequest;
import com.thiagomf.interviewschedulerapi.dto.UserResponse;
import com.thiagomf.interviewschedulerapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        return userService.getCurrentUser(authentication.getName());
    }
}