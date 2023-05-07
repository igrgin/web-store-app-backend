package com.web.store.app.backend.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "spring.security.web")
public record WebConfigProperties(Config config) {

    public record Config(String allowedCorsOrigins,String mapping){}
}
