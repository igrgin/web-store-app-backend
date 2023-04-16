package com.web.store.app.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {

    @Size(max = 256)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(?:\\.[a-zA-Z]{2,})?$")
    private String email;
    @Size(max = 256)
    @JsonAlias(value = "first_name")
    private String firstName;
    @Size(max = 256)
    private String lastName;
    @Size(max = 256)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}|^(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{8,}|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}|^(?=.*[a-z])(?=.*\\d)(?=.*\\W).{8,}$")
    private String password;
}
