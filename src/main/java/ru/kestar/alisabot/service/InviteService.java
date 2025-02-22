package ru.kestar.alisabot.service;

import java.util.List;
import ru.kestar.alisabot.model.entity.Invite;

public interface InviteService {

    Invite createNewInvite(Long telegramUserId);

    List<Invite> getInvitesByOwner(Long ownerId);

    boolean removeInviteCode(Long telegramId, String code);
}
