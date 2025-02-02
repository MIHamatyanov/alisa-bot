package ru.kestar.alisabot.service;

import ru.kestar.alisabot.model.dto.yandex.response.SmartHouseInfo;

public interface YandexService {

    SmartHouseInfo getSmartHouseInfo(String telegramChatId);

    void executeGroupAction(String telegramChatId, String groupId, boolean state);

    void executeDeviceAction(String telegramChatId, String deviceId, boolean state);
}
