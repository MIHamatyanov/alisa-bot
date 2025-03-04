package ru.kestar.alisabot.service.impl;

import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kestar.alisabot.model.entity.Invite;
import ru.kestar.alisabot.model.entity.BotUser;
import ru.kestar.alisabot.repository.InviteRepository;
import ru.kestar.alisabot.service.InviteService;
import ru.kestar.alisabot.service.BotUserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {
    private final BotUserService botUserService;
    private final InviteRepository inviteRepository;

    @Override
    public Invite createNewInvite(Long telegramUserId) {
        final BotUser user = botUserService.getUserByTelegramId(telegramUserId);

        final Invite invite = new Invite();
        invite.setOwner(user);
        invite.setCreatedAt(LocalDateTime.now());
        invite.setCode(UUID.randomUUID().toString());
        return inviteRepository.save(invite);
    }

    @Override
    public List<Invite> getInvitesByOwner(Long ownerId) {
        return inviteRepository.findUserInvites(ownerId);
    }

    @Override
    @Transactional
    public boolean removeInviteCode(Long telegramId, String code) {
        final Optional<Invite> inviteOpt = inviteRepository.findByOwnerTelegramIdAndCode(telegramId, code);
        if (inviteOpt.isEmpty()) {
            return false;
        }

        final Invite invite = inviteOpt.get();
        if (nonNull(invite.getUsedBy())) {
            botUserService.logoutUser(invite.getUsedBy().getTelegramId());
        }
        inviteRepository.delete(invite);
        return true;
    }
}
