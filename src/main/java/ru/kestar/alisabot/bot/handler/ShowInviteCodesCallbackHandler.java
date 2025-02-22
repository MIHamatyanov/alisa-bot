package ru.kestar.alisabot.bot.handler;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.kestar.alisabot.bot.menu.MenuBuilder;
import ru.kestar.alisabot.model.entity.Invite;
import ru.kestar.alisabot.model.enums.TelegramCallbackAction;
import ru.kestar.alisabot.service.InviteService;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.UpdateHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowInviteCodesCallbackHandler implements UpdateHandler {
    private static final String INVITE_CODES_MESSAGE = """
        Список кодов приглашения:
        
        %s
        """;
    private static final String INVITE_CODE_FORMAT = "%s) <pre>%s</pre>\n";

    private final InviteService inviteService;
    private final MenuBuilder menuBuilder;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final List<Invite> invites = inviteService.getInvitesByOwner(context.getUser().getId());

        StringJoiner inviteCodes = new StringJoiner("\n");
        for (int i = 0; i < invites.size(); i++) {
            final Invite invite = invites.get(i);
            inviteCodes.add(INVITE_CODE_FORMAT.formatted(i + 1, invite.getCode()));
        }

        final EditMessageText response = EditMessageText.builder()
            .chatId(context.getChatId())
            .messageId(context.getCallbackData().getMessageId())
            .text(INVITE_CODES_MESSAGE.formatted(inviteCodes.toString()))
            .parseMode("HTML")
            .replyMarkup(menuBuilder.buildInviteCodesMenu())
            .build();
        return Optional.of(response);
    }

    @Override
    public List<String> getSupportedCallbacks() {
        return List.of(TelegramCallbackAction.SHOW_INVITE_CODE.name());
    }

}
