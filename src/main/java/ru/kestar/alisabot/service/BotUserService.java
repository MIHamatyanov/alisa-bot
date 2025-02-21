package ru.kestar.alisabot.service;

import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.model.entity.BotUser;

public interface BotUserService {

    void signInUser(String telegramId, YandexTokenInfo tokenInfo);

    BotUser getUserByTelegramId(String telegramId);

    void logoutUser(String telegramId);
}
