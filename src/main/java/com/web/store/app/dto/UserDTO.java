package com.web.store.app.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.web.store.app.security.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserDTO(@NotNull @Positive Long id,
                      @Size(max = 256) @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(?:\\.[a-zA-Z]{2,})?$") String email,
                      @Size(max = 256) String username,
                      @Size(max = 256) @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}|^(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{8,}|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}|^(?=.*[a-z])(?=.*\\d)(?=.*\\W).{8,}$") String password,
                      UserRole role) {
}