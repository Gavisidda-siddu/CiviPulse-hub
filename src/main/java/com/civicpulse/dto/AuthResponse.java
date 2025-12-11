package com.civicpulse.dto;

import com.civicpulse.model.Role;
import com.civicpulse.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
}
