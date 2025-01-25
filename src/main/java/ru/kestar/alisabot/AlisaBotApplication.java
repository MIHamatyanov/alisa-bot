package ru.kestar.alisabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.kestar.alisabot.config.properties.ApplicationProperties;

@EnableWebSecurity
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class AlisaBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlisaBotApplication.class, args);
    }

}
