package ru.kestar.alisabot.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kestar.alisabot.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(Long telegramId);

}
