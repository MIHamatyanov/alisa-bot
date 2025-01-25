package ru.kestar.alisabot.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "application.properties")
public class ApplicationProperties {
    private String baseUrl;
}
