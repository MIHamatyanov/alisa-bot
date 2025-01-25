package ru.kestar.alisabot.security.storage;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;

@Component
public class InMemoryTokenStorage implements TokenStorage {
    private final Map<String, YandexTokenInfo> tokens = new ConcurrentHashMap<>();

    @Override
    public void store(String telegramId, YandexTokenInfo tokenInfo) {
        tokens.put(telegramId, tokenInfo);
    }

    @Override
    public Optional<YandexTokenInfo> get(String telegramId) {
        return Optional.ofNullable(tokens.get(telegramId));
    }

    @Override
    public void revokeToken(String telegramId) {
        tokens.remove(telegramId);
    }
}
