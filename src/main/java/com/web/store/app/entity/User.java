package com.web.store.app.entity;

import com.web.store.app.util.shared.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Size(max = 256)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
