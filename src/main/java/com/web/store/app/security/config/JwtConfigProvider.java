package com.web.store.app.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "spring.security.jwt")
@Data
public class JwtConfigProvider {

    private String secretKey;


}
