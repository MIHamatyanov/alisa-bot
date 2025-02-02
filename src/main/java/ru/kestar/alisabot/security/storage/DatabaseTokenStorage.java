package ru.kestar.alisabot.security.storage;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.model.entity.User;
import ru.kestar.alisabot.repository.UserRepository;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class DatabaseTokenStorage implements TokenStorage {
    private final UserRepository userRepository;

    @Override
    public void store(String telegramId, YandexTokenInfo tokenInfo) {
        final Long telegramIdLong = Long.valueOf(telegramId);
        final User user = userRepository.findByTelegramId(telegramIdLong)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setTelegramId(telegramIdLong);
                return newUser;
            });

        log.info("User with telegramId {} {}.", telegramId, user.getId() == null ? "not exists. Creating" : "already exists. Updating");

        user.setLogin(tokenInfo.getLogin());
        user.setToken(tokenInfo.getAccessToken());
        user.setLoginDate(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public Optional<YandexTokenInfo> get(String telegramId) {
        return userRepository.findByTelegramId(Long.valueOf(telegramId))
            .filter(user -> user.getToken() != null)
            .map(user -> YandexTokenInfo.builder()
                .login(user.getLogin())
                .accessToken(user.getToken())
                .build()
            );
    }

    @Override
    public void revokeToken(String telegramId) {
        final Optional<User> userOpt = userRepository.findByTelegramId(Long.valueOf(telegramId));
        if (userOpt.isEmpty()) {
            return;
        }

        final User user = userOpt.get();
        user.setLogin(null);
        user.setToken(null);
        userRepository.save(user);
    }
}
