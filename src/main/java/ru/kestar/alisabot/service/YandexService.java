package ru.kestar.alisabot.service;

import ru.kestar.alisabot.model.dto.yandex.SmartHouseInfo;

public interface YandexService {

    SmartHouseInfo getSmartHouseInfo(String telegramChatId);
}
