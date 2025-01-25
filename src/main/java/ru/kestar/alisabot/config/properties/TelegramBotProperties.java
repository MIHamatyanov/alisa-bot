package ru.kestar.alisabot.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "application.telegram.bot")
public class TelegramBotProperties {
    private String token;
    private String username;
}
