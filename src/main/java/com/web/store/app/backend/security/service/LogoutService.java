package com.web.store.app.backend.security.service;

import com.web.store.app.backend.authentication.repository.TokenRepository;
import com.web.store.app.backend.user.service.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final var authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        jwt = authHeader.substring(7);
        var userEmail = jwtService.extractUsername(jwt);
        Optional.ofNullable(tokenRepository.findTokenByUser_Email(userEmail)).
                ifPresent(storedToken -> tokenRepository.deleteTokenByToken(storedToken.getToken()));
    }
}
