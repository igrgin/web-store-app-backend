package com.web.store.app.backend.security.properties;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebConfigPropertiesProvider {

    private final WebConfigProperties configProperties;

    public WebConfigProperties.Config getConfig()
    {

        return configProperties.config();
    }
}
