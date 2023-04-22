package com.web.store.app.backend.security.config;

import com.web.store.app.backend.security.properties.JwtConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtConfigProperties.class)
public class JwtConfig {
}
