package com.web.store.app.backend.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;


public interface JwtService {


    String extractUsername(String token);

    String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expiration);

    String generateAccessToken(UserDetails userDetails);

    String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);

    Boolean isTokenValid(String token, UserDetails userDetails);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);


    Claims extractAllClaims(String token);

    boolean isTokenExpired(String token);
}
