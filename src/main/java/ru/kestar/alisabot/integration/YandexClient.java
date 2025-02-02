package ru.kestar.alisabot.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.kestar.alisabot.exception.YandexIntegrationException;
import ru.kestar.alisabot.model.dto.yandex.request.ExecuteDeviceActionsRequest;
import ru.kestar.alisabot.model.dto.yandex.request.ExecuteGroupActionsRequest;
import ru.kestar.alisabot.model.dto.yandex.response.SmartHouseInfo;

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
                .header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader(token))
                .retrieve()
                .body(SmartHouseInfo.class);
        } catch (Exception e) {
            log.error("Error occurred while getting smart house info from Yandex", e);
            throw new YandexIntegrationException(e.getMessage());
        }
    }

    public SmartHouseInfo executeGroupAction(String token, String groupId, ExecuteGroupActionsRequest request) {
        try {
            return restClient.post()
                .uri(uriBuilder -> uriBuilder
                    .path("/groups/{groupId}/actions")
                    .build(groupId)
                )
                .header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader(token))
                .body(request)
                .retrieve()
                .body(SmartHouseInfo.class);
        } catch (Exception e) {
            log.error("Error occurred while executing group action", e);
            throw new YandexIntegrationException(e.getMessage());
        }
    }

    public SmartHouseInfo executeDeviceAction(String token, ExecuteDeviceActionsRequest request) {
        try {
            return restClient.post()
                .uri("/devices/actions")
                .header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader(token))
                .body(request)
                .retrieve()
                .body(SmartHouseInfo.class);
        } catch (Exception e) {
            log.error("Error occurred while executing group action", e);
            throw new YandexIntegrationException(e.getMessage());
        }
    }

    private String createAuthorizationHeader(String token) {
        return "Bearer " + token;
    }
}
