package com.web.store.app.backend.security.config;

import com.web.store.app.backend.security.properties.JwtConfigProperties;
import com.web.store.app.backend.security.properties.WebConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({WebConfigProperties.class, JwtConfigProperties.class})
public class WebPropertiesConfig {
}
