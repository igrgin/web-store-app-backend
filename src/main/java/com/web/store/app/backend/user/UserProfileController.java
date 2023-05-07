package com.web.store.app.backend.user;

import com.web.store.app.backend.security.service.JwtService;
import com.web.store.app.backend.user.dto.UserProfileDTO;
import com.web.store.app.backend.user.entity.AppUser;
import com.web.store.app.backend.user.service.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/api")
public class UserProfileController {

    private final AppUserService userService;

    private final JwtService jwtService;

    @PostMapping("email")
    private ResponseEntity<UserProfileDTO> getUserByEmail(@RequestBody String email) {
        return userService.findByEmail(email).map(user -> ResponseEntity.status(HttpStatus.OK)
                        .body(mapAppUserToUserProfile(user)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @GetMapping("user")
    private ResponseEntity<UserProfileDTO> getUserByToken(HttpServletRequest request) {
        final var userEmail = jwtService.extractUsername(request.getHeader(HttpHeaders.AUTHORIZATION)
                .substring(7));
        return userService.findByEmail(userEmail).map(user -> ResponseEntity.status(HttpStatus.OK)
                        .body(mapAppUserToUserProfile(user)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    private UserProfileDTO mapAppUserToUserProfile(AppUser user) {
        return new UserProfileDTO(user.getFirstName(), user.getLastName(), user.getRole(),user.getEmail());
    }
}
