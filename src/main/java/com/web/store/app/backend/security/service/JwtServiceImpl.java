package com.web.store.app.backend.security.service;

import com.web.store.app.backend.security.properties.JwtConfigPropertiesProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    private JwtConfigPropertiesProvider jwtPropertiesProvider;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    @Override
    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expiration) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512).compact();
    }
    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtPropertiesProvider.getConfig().accessExpiration());
    }
    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        System.out.println(jwtPropertiesProvider.getConfig().refreshExpiration());
        return buildToken(extraClaims, userDetails, jwtPropertiesProvider.getConfig().refreshExpiration());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final var username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build().parseClaimsJws(token)
                .getBody();
    }

    @Override
    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        var keyBytes = Decoders.BASE64.decode(jwtPropertiesProvider.getConfig().secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}