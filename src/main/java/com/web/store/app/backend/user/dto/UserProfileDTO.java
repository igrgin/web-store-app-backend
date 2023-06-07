package com.web.store.app.backend.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.store.app.backend.user.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record UserProfileDTO(
        Long id,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @Enumerated(EnumType.STRING)
        Role role,
        String email) {
}
