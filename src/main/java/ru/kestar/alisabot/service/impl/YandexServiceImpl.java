package ru.kestar.alisabot.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kestar.alisabot.integration.YandexClient;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.model.dto.yandex.request.ActionInfo;
import ru.kestar.alisabot.model.dto.yandex.request.DeviceActionsInfo;
import ru.kestar.alisabot.model.dto.yandex.request.ExecuteDeviceActionsRequest;
import ru.kestar.alisabot.model.dto.yandex.request.ExecuteGroupActionsRequest;
import ru.kestar.alisabot.model.dto.yandex.response.SmartHouseInfo;
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

    @Override
    public void executeGroupAction(String telegramChatId, String groupId, boolean state) {
        final Optional<YandexTokenInfo> tokenInfoOpt = tokenStorage.get(telegramChatId);
        if (tokenInfoOpt.isEmpty()) {
            return;
        }

        final ExecuteGroupActionsRequest request = new ExecuteGroupActionsRequest(
            List.of(buildOnOffAction(state))
        );
        yandexClient.executeGroupAction(tokenInfoOpt.get().getAccessToken(), groupId, request);
    }

    @Override
    public void executeDeviceAction(String telegramChatId, String deviceId, boolean state) {
        final Optional<YandexTokenInfo> tokenInfoOpt = tokenStorage.get(telegramChatId);
        if (tokenInfoOpt.isEmpty()) {
            return;
        }

        final ExecuteDeviceActionsRequest request = new ExecuteDeviceActionsRequest(
            List.of(new DeviceActionsInfo(deviceId, List.of(buildOnOffAction(state))))
        );
        yandexClient.executeDeviceAction(tokenInfoOpt.get().getAccessToken(), request);
    }

    private ActionInfo buildOnOffAction(boolean state) {
        final Map<String, Object> actionState = Map.of(
            "instance", "on",
            "value", state
        );

        return new ActionInfo("devices.capabilities.on_off", actionState);
    }
}
