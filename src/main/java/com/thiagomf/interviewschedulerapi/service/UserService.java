package com.thiagomf.interviewschedulerapi.service;

import com.thiagomf.interviewschedulerapi.dto.RegisterRequest;
import com.thiagomf.interviewschedulerapi.dto.UserResponse;
import com.thiagomf.interviewschedulerapi.entity.User;
import com.thiagomf.interviewschedulerapi.exception.EmailAlreadyExistsException;
import com.thiagomf.interviewschedulerapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }
}