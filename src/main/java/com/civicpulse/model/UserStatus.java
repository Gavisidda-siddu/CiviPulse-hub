package com.civicpulse.model;

public enum UserStatus {
    ACTIVE,     // can login
    PENDING,    // waiting for admin approval (officer)
    BLOCKED
}
