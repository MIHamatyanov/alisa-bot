package ru.kestar.alisabot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kestar.alisabot.model.entity.Invite;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    @Query("""
    select i from Invite i
        left join fetch i.usedBy
    order by i.createdAt asc
    """)
    List<Invite> findUserInvites(Long ownerId);

    Optional<Invite> findByOwnerTelegramIdAndCode(Long ownerId, String code);
}
