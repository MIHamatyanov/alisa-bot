package ru.kestar.alisabot.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kestar.alisabot.exception.UserNotFoundException;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.model.entity.User;
import ru.kestar.alisabot.repository.UserRepository;
import ru.kestar.alisabot.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void signInUser(String telegramId, YandexTokenInfo tokenInfo) {
        final Long telegramIdLong = Long.parseLong(telegramId);
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
    public User getUserByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(Long.parseLong(telegramId))
            .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void logoutUser(String telegramId) {
        final Optional<User> userOpt = userRepository.findByTelegramId(Long.valueOf(telegramId));
        if (userOpt.isEmpty()) {
            return;
        }

        final User user = userOpt.get();
        user.setLogin(null);
        user.setToken(null);
        user.setParent(null);
        userRepository.save(user);
    }
}
