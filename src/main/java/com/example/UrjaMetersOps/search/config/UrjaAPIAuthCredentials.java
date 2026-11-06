package com.example.UrjaMetersOps.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "urja.api")
@Data
public class UrjaAPIAuthCredentials {
    private String email;
    private String password;
}
