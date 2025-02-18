package ru.kestar.alisabot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kestar.alisabot.model.entity.Invite;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    List<Invite> findAllByOwnerTelegramId(Long ownerId);

    Optional<Invite> findByOwnerTelegramIdAndCode(Long ownerId, String code);
}
