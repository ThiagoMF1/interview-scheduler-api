package com.thiagomf.interviewschedulerapi.service;

import com.thiagomf.interviewschedulerapi.dto.AuthResponse;
import com.thiagomf.interviewschedulerapi.dto.LoginRequest;
import com.thiagomf.interviewschedulerapi.dto.RegisterRequest;
import com.thiagomf.interviewschedulerapi.dto.UserResponse;
import com.thiagomf.interviewschedulerapi.entity.User;
import com.thiagomf.interviewschedulerapi.exception.EmailAlreadyExistsException;
import com.thiagomf.interviewschedulerapi.exception.InvalidCredentialsException;
import com.thiagomf.interviewschedulerapi.exception.ResourceNotFoundException;
import com.thiagomf.interviewschedulerapi.repository.UserRepository;
import com.thiagomf.interviewschedulerapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}