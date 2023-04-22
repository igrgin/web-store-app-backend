package com.web.store.app.backend.service;

import com.web.store.app.backend.dto.AuthenticationRequest;
import com.web.store.app.backend.dto.AuthenticationResponse;
import com.web.store.app.backend.dto.RegisterRequest;
import com.web.store.app.backend.entity.AppUser;
import com.web.store.app.backend.entity.Token;
import com.web.store.app.backend.model.TokenType;
import com.web.store.app.backend.model.UserRole;
import com.web.store.app.backend.repository.sql.CustomerRepository;
import com.web.store.app.backend.repository.sql.TokenRepository;
import com.web.store.app.backend.security.JwtService;
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

    private final CustomerRepository repository;
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
        var customer = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).role(UserRole.USER)
                .build();

        repository.save(customer);
        var jwtToken = jwtService.generateToken(customer);
        saveCustomerToken(customer, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var customer = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("AppUser not found"));
        var jwtToken = jwtService.generateToken(customer);
        deleteAllValidTokensByCustomer(customer);
        saveCustomerToken(customer, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private void deleteAllValidTokensByCustomer(AppUser customers) {
        var validTokens = tokenRepository.findAllTokensByUser(customers.getId());

        if (validTokens.isEmpty()) return;

        tokenRepository.deleteTokensByTokenIn(validTokens.stream().map(Token::getToken).toList());
    }

    private void saveCustomerToken(AppUser customers, String jwtToken) {
        var token = Token.builder()
                .user(customers)
                .tokenType(TokenType.BEARER)
                .token(jwtToken)
                .build();
        tokenRepository.save(token);
    }

}
