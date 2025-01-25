package ru.kestar.alisabot.security.storage;

import java.util.Optional;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;

public interface TokenStorage {

    void store(String telegramId, YandexTokenInfo tokenInfo);

    Optional<YandexTokenInfo> get(String telegramId);

    void revokeToken(String telegramId);
}
