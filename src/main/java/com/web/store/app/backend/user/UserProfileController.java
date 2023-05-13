package com.web.store.app.backend.user;

import com.web.store.app.backend.security.service.JwtService;
import com.web.store.app.backend.user.dto.UserProfileDTO;
import com.web.store.app.backend.user.service.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api")
public class UserProfileController {

    private final AppUserService userService;

    private final JwtService jwtService;

    @GetMapping("/private/user")
    private ResponseEntity<UserProfileDTO> getUserByToken(HttpServletRequest request) {
        final var userEmail = jwtService.extractUsername(request.getHeader(HttpHeaders.AUTHORIZATION)
                .substring(7));
        return userService.findByEmailToDTO(userEmail).map(user -> ResponseEntity.status(HttpStatus.OK)
                        .body(user))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
