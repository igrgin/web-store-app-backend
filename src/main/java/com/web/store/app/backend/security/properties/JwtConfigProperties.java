package com.web.store.app.backend.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "spring.security.jwt")
public record JwtConfigProperties(Config config) {

    public record Config(String secretKey){}
}
