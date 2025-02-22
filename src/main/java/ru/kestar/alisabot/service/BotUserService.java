package ru.kestar.alisabot.service;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.model.entity.BotUser;

public interface BotUserService {

    BotUser getOrCreateUser(User telegramUser);

    void signInUserWithYandex(Long telegramId, YandexTokenInfo tokenInfo);

    BotUser getUserByTelegramId(Long telegramId);

    void logoutUser(Long telegramUserId);
}
