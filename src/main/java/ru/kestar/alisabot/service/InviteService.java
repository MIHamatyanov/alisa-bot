package ru.kestar.alisabot.service;

import java.util.List;
import ru.kestar.alisabot.model.entity.Invite;

public interface InviteService {

    Invite createNewInvite(String telegramUserId);

    List<Invite> getInvitesByOwner(String ownerId);

    boolean removeInviteCode(String telegramId, String code);
}
