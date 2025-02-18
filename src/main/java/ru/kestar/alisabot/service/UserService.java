package ru.kestar.alisabot.service;

import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.model.entity.User;

public interface UserService {

    void signInUser(String telegramId, YandexTokenInfo tokenInfo);

    User getUserByTelegramId(String telegramId);

    void logoutUser(String telegramId);
}
