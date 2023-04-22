package com.web.store.app.backend.security.properties;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtConfigPropertiesProvider {

    private final JwtConfigProperties configProperties;

    public String getSecretKey()
    {
        return configProperties.config().secretKey();
    }

}
