package ru.kestar.alisabot.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.kestar.alisabot.exception.YandexIntegrationException;
import ru.kestar.alisabot.model.dto.yandex.SmartHouseInfo;

@Slf4j
@Component
public class YandexClient {
    private final RestClient restClient;

    public YandexClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
            .baseUrl("https://api.iot.yandex.net/v1.0") //TODO вынести в конфиг
            .build();
    }

    public SmartHouseInfo getSmartHouseInfo(String token) {
        try {
            return restClient.get()
                .uri("/user/info")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(SmartHouseInfo.class);
        } catch (Exception e) {
            log.error("Error occurred while getting smart house info from Yandex", e);
            throw new YandexIntegrationException(e.getMessage());
        }
    }
}
