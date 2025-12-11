package com.civicpulse.service;

import com.civicpulse.config.JwtUtil;
import com.civicpulse.dto.*;
import com.civicpulse.model.*;
import com.civicpulse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // 1. create single admin on startup (called from main class)
    public void createDefaultAdminIfNotExists() {
        if (!userRepository.existsByRole(Role.ADMIN)) {
            User admin = User.builder()
                    .name("System Admin")
                    .email("admin@civicpulse.com")    // you can change
                    .password(passwordEncoder.encode("admin123")) // change
                    .role(Role.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
        }
    }

    // CITIZEN REGISTER
    public void registerCitizen(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CITIZEN)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    // OFFICER REGISTER (PENDING)
    public void registerOfficer(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.OFFICER)
                .department(request.getDepartment())
                .status(UserStatus.PENDING) // need admin approval
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    // ADMIN approves OFFICER
    public void approveOfficer(Long officerId) {
        User user = userRepository.findById(officerId)
                .orElseThrow(() -> new RuntimeException("Officer not found"));
        if (user.getRole() != Role.OFFICER) {
            throw new RuntimeException("User is not an officer");
        }
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    // LOGIN for all (ADMIN / CITIZEN / OFFICER)
    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            // officer still pending, or blocked
            throw new RuntimeException("Account is not active: " + user.getStatus());
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }
}
