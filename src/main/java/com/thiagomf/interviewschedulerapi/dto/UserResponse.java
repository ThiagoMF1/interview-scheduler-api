package com.thiagomf.interviewschedulerapi.dto;

import com.thiagomf.interviewschedulerapi.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
}