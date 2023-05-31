package com.web.store.app.backend.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.store.app.backend.authentication.model.AuthenticationRequest;
import com.web.store.app.backend.authentication.model.AuthenticationResponse;
import com.web.store.app.backend.authentication.model.RegisterRequest;
import com.web.store.app.backend.authentication.entity.Token;
import com.web.store.app.backend.authentication.entity.TokenType;
import com.web.store.app.backend.authentication.repository.TokenRepository;
import com.web.store.app.backend.security.service.JwtService;
import com.web.store.app.backend.user.entity.AppUser;
import com.web.store.app.backend.user.entity.Role;
import com.web.store.app.backend.user.service.AppUserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AppUserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public void register(RegisterRequest request) {
        var appUser = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        if (appUser.getEmail().equals("grginivo@gmail.com")) {
            appUser.setRole(Role.ADMIN);
        }

        userService.save(appUser);
    }

    public Optional<AuthenticationResponse> authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var appUser = userService.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("AppUser not found"));
        var jwtToken = jwtService.generateAccessToken(appUser);
        var refreshToken = jwtService.generateRefreshToken(appUser);
        deleteTokenByUser(appUser);
        saveAppUserToken(appUser, jwtToken);
        return Optional.ofNullable(AuthenticationResponse.builder()
                .accessToken(jwtToken).refreshToken(refreshToken).build());
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userService.findByEmail(userEmail).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, user))
            {
                deleteTokenByUser(user);
                var accessToken = jwtService.generateAccessToken(user);
                saveAppUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .refreshToken(refreshToken).accessToken(accessToken).build();

                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);

            }
        }
    }

    private void deleteTokenByUser(AppUser appUsers) {
        var validTokens = tokenRepository.findAllTokensByUser(appUsers.getId());

        if (validTokens.isEmpty()) return;

        tokenRepository.deleteTokensByTokenIn(validTokens.stream().map(Token::getToken).toList());
    }

    private void saveAppUserToken(AppUser appUsers, String jwtToken) {
        var accessToken = Token.builder()
                .user(appUsers)
                .tokenType(TokenType.BEARER)
                .token(jwtToken)
                .build();
        tokenRepository.save(accessToken);
    }

}
