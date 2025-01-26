package ru.kestar.alisabot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kestar.alisabot.integration.YandexClient;
import ru.kestar.alisabot.model.dto.yandex.SmartHouseInfo;
import ru.kestar.alisabot.security.storage.TokenStorage;
import ru.kestar.alisabot.service.YandexService;

@Service
@RequiredArgsConstructor
public class YandexServiceImpl implements YandexService {
    private final YandexClient yandexClient;
    private final TokenStorage tokenStorage;

    @Override
    public SmartHouseInfo getSmartHouseInfo(String telegramChatId) {
        return tokenStorage.get(telegramChatId)
            .map(tokenInfo -> yandexClient.getSmartHouseInfo(tokenInfo.getAccessToken()))
            .orElse(null);
    }
}
