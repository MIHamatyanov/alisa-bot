package ru.kestar.alisabot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveUserInfo {
    private String chatId;
    private YandexTokenInfo tokenInfo;

    public boolean isAuthenticated() {
        return tokenInfo != null;
    }
}
