package ru.kestar.alisabot.service;

import ru.kestar.alisabot.model.dto.yandex.response.SmartHouseInfo;

public interface YandexService {

    SmartHouseInfo getSmartHouseInfo(Long telegramUserId);

    void executeGroupAction(Long telegramUserId, String groupId, boolean state);

    void executeDeviceAction(Long telegramUserId, String deviceId, boolean state);
}
