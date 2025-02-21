package ru.kestar.alisabot.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    public void signInUser(String telegramId, YandexTokenInfo tokenInfo) {
        final Long telegramIdLong = Long.parseLong(telegramId);
        final BotUser user = botUserRepository.findByTelegramId(telegramIdLong)
            .orElseGet(() -> {
                BotUser newUser = new BotUser();
                newUser.setTelegramId(telegramIdLong);
                return newUser;
            });

        log.info("User with telegramId {} {}.", telegramId, user.getId() == null ? "not exists. Creating" : "already exists. Updating");

        user.setLogin(tokenInfo.getLogin());
        user.setToken(tokenInfo.getAccessToken());
        user.setLoginDate(LocalDateTime.now());

        botUserRepository.save(user);
    }

    @Override
    public BotUser getUserByTelegramId(String telegramId) {
        return botUserRepository.findByTelegramId(Long.parseLong(telegramId))
            .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void logoutUser(String telegramId) {
        final Optional<BotUser> userOpt = botUserRepository.findByTelegramId(Long.valueOf(telegramId));
        if (userOpt.isEmpty()) {
            return;
        }

        final BotUser user = userOpt.get();
        user.setLogin(null);
        user.setToken(null);
        user.setParent(null
        botUserRepository.save(user);
    }
}
