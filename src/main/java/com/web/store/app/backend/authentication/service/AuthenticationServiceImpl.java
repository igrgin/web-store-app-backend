package com.web.store.app.backend.authentication.service;

import com.web.store.app.backend.authentication.dto.AuthenticationRequest;
import com.web.store.app.backend.authentication.dto.AuthenticationResponse;
import com.web.store.app.backend.authentication.dto.RegisterRequest;
import com.web.store.app.backend.authentication.entity.AppUser;
import com.web.store.app.backend.authentication.entity.Token;
import com.web.store.app.backend.authentication.entity.TokenType;
import com.web.store.app.backend.authentication.entity.UserRole;
import com.web.store.app.backend.authentication.repository.AppUserRepository;
import com.web.store.app.backend.authentication.repository.TokenRepository;
import com.web.store.app.backend.security.service.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @PostConstruct
    private void deleteAllInvalidTokens() {
        log.info("Deleting invalid tokens");
        tokenRepository.deleteTokensByTokenIn(tokenRepository.findAll()
                .stream()
                .map(Token::getToken)
                .filter(jwtService::isTokenExpired).toList());
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var appUser = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).role(UserRole.USER)
                .build();

        repository.save(appUser);
        var jwtToken = jwtService.generateToken(appUser);
        saveAppUserToken(appUser, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var appUser = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("AppUser not found"));
        var jwtToken = jwtService.generateToken(appUser);
        deleteAllValidTokensByAppUser(appUser);
        saveAppUserToken(appUser, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void deleteAllValidTokensByAppUser(AppUser appUsers) {
        var validTokens = tokenRepository.findAllTokensByUser(appUsers.getId());

        if (validTokens.isEmpty()) return;

        tokenRepository.deleteTokensByTokenIn(validTokens.stream().map(Token::getToken).toList());
    }

    private void saveAppUserToken(AppUser appUsers, String jwtToken) {
        var token = Token.builder()
                .user(appUsers)
                .tokenType(TokenType.BEARER)
                .token(jwtToken)
                .build();
        tokenRepository.save(token);
    }

}
