package ru.kestar.alisabot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kestar.alisabot.model.entity.Invite;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    List<Invite> findAllByOwnerTelegramId(Long ownerId);
}
