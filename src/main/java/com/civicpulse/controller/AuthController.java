package com.civicpulse.controller;

import com.civicpulse.dto.*;
import com.civicpulse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin // adjust origins if needed
public class AuthController {

    private final UserService userService;

    // CITIZEN registration
    @PostMapping("/register/citizen")
    public ResponseEntity<String> registerCitizen(@Valid @RequestBody RegisterRequest request) {
        userService.registerCitizen(request);
        return ResponseEntity.ok("Citizen registered successfully");
    }

    // OFFICER registration (PENDING)
    @PostMapping("/register/officer")
    public ResponseEntity<String> registerOfficer(@Valid @RequestBody RegisterRequest request) {
        userService.registerOfficer(request);
        return ResponseEntity.ok("Officer registered, waiting for admin approval");
    }

    // LOGIN (admin / citizen / officer)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // ADMIN approves officer
    @PutMapping("/officers/{id}/approve")
    public ResponseEntity<String> approveOfficer(@PathVariable Long id) {
        userService.approveOfficer(id);
        return ResponseEntity.ok("Officer approved");
    }
}
