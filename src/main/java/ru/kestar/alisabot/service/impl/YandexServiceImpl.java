package ru.kestar.alisabot.service.impl;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kestar.alisabot.integration.YandexClient;
import ru.kestar.alisabot.model.dto.yandex.request.ActionInfo;
import ru.kestar.alisabot.model.dto.yandex.request.DeviceActionsInfo;
import ru.kestar.alisabot.model.dto.yandex.request.ExecuteDeviceActionsRequest;
import ru.kestar.alisabot.model.dto.yandex.request.ExecuteGroupActionsRequest;
import ru.kestar.alisabot.model.dto.yandex.response.SmartHouseInfo;
import ru.kestar.alisabot.model.entity.BotUser;
import ru.kestar.alisabot.service.BotUserService;
import ru.kestar.alisabot.service.YandexService;

@Service
@RequiredArgsConstructor
public class YandexServiceImpl implements YandexService {
    private final YandexClient yandexClient;
    private final BotUserService botUserService;

    @Override
    public SmartHouseInfo getSmartHouseInfo(String telegramChatId) {
        final BotUser user = botUserService.getUserByTelegramId(telegramChatId);
        return yandexClient.getSmartHouseInfo(user.getToken());
    }

    @Override
    public void executeGroupAction(String telegramChatId, String groupId, boolean state) {
        final BotUser user = botUserService.getUserByTelegramId(telegramChatId);

        final ExecuteGroupActionsRequest request = new ExecuteGroupActionsRequest(
            List.of(buildOnOffAction(state))
        );
        yandexClient.executeGroupAction(user.getToken(), groupId, request);
    }

    @Override
    public void executeDeviceAction(String telegramChatId, String deviceId, boolean state) {
        final BotUser user = botUserService.getUserByTelegramId(telegramChatId);

        final ExecuteDeviceActionsRequest request = new ExecuteDeviceActionsRequest(
            List.of(new DeviceActionsInfo(deviceId, List.of(buildOnOffAction(state))))
        );
        yandexClient.executeDeviceAction(user.getToken(), request);
    }

    private ActionInfo buildOnOffAction(boolean state) {
        final Map<String, Object> actionState = Map.of(
            "instance", "on",
            "value", state
        );

        return new ActionInfo("devices.capabilities.on_off", actionState);
    }
}
