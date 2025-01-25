package ru.kestar.alisabot.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YandexTokenInfo {
    private String login;
    private String accessToken;
}
