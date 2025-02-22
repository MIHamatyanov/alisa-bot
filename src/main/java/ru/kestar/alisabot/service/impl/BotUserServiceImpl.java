package ru.kestar.alisabot.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.kestar.alisabot.exception.UserNotFoundException;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.model.entity.BotUser;
import ru.kestar.alisabot.repository.BotUserRepository;
import ru.kestar.alisabot.service.BotUserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotUserServiceImpl implements BotUserService {

    private final BotUserRepository botUserRepository;

    @Override
    public BotUser getOrCreateUser(User telegramUser) {
        return botUserRepository.findByTelegramId(telegramUser.getId())
            .orElseGet(() -> createUser(telegramUser));
    }

    private BotUser createUser(User telegramUser) {
        log.info("Create new user with telegram id: {}", telegramUser.getId());

        final BotUser botUser = new BotUser();
        botUser.setTelegramId(telegramUser.getId());
        botUser.setFirstName(telegramUser.getFirstName());
        botUser.setLastName(telegramUser.getLastName());
        botUser.setUserName(telegramUser.getUserName());

        return botUserRepository.save(botUser);
    }

    @Override
    public void signInUserWithYandex(Long telegramId, YandexTokenInfo tokenInfo) {
        final BotUser user = getUserByTelegramId(telegramId);

        log.info("Sign in user with telegram id: {}. Yandex login - {}", telegramId, tokenInfo.getLogin());
        user.setLogin(tokenInfo.getLogin());
        user.setToken(tokenInfo.getAccessToken());
        user.setLoginDate(LocalDateTime.now());

        botUserRepository.save(user);
    }

    @Override
    public BotUser getUserByTelegramId(Long telegramId) {
        return botUserRepository.findByTelegramId(telegramId)
            .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void logoutUser(Long telegramUserId) {
        final Optional<BotUser> userOpt = botUserRepository.findByTelegramId(telegramUserId);
        if (userOpt.isEmpty()) {
            return;
        }

        final BotUser user = userOpt.get();
        user.setLogin(null);
        user.setToken(null);
        user.setParent(null);
        botUserRepository.save(user);
    }
}
