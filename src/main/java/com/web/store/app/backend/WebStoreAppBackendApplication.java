package com.web.store.app.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WebStoreAppBackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(WebStoreAppBackendApplication.class, args);

    }


}
