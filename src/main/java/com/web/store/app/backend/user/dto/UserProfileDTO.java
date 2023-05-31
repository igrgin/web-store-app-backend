package com.web.store.app.backend.user.dto;

import com.web.store.app.backend.user.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record UserProfileDTO(
        Long id,
        @Column(name = "first_name")
        String firstName,
        @Column(name = "last_name")
        String lastName,
        @Enumerated(EnumType.STRING)
        Role role,
        String email) {
}
