package ru.kestar.alisabot.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kestar.alisabot.model.entity.Invite;
import ru.kestar.alisabot.model.entity.User;
import ru.kestar.alisabot.repository.InviteRepository;
import ru.kestar.alisabot.service.InviteService;
import ru.kestar.alisabot.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {
    private final UserService userService;
    private final InviteRepository inviteRepository;

    @Override
    public Invite createNewInvite(String telegramUserId) {
        final User user = userService.getUserByTelegramId(telegramUserId);

        final Invite invite = new Invite();
        invite.setOwner(user);
        invite.setCreatedAt(LocalDateTime.now());
        invite.setCode(UUID.randomUUID().toString());
        return inviteRepository.save(invite);
    }

    @Override
    public List<Invite> getInvitesByOwner(String ownerId) {
        return inviteRepository.findAllByOwnerTelegramId(Long.parseLong(ownerId));
    }
}
