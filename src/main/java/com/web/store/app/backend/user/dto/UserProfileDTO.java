package com.web.store.app.backend.user.dto;

import com.web.store.app.backend.user.entity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record UserProfileDTO(
        @Column(name = "first_name") String firstName,


        @Column(name = "last_name")
        String lastName,
        @Enumerated(EnumType.STRING)
        UserRole role, String email) {
}
