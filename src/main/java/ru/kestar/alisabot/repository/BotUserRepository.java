package ru.kestar.alisabot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kestar.alisabot.model.entity.BotUser;

public interface BotUserRepository extends JpaRepository<BotUser, Long> {

    Optional<BotUser> findByTelegramId(Long telegramId);

}
