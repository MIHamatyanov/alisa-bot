package ru.kestar.alisabot.bot.handler;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.kestar.alisabot.model.entity.Invite;
import ru.kestar.alisabot.model.enums.TelegramCallbackAction;
import ru.kestar.alisabot.service.InviteService;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.UpdateHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateInviteCodeCallbackHandler implements UpdateHandler {
    private static final String INVITE_CODE_GENERATED_MESSAGE = """
        Код приглашения сгенерирован.
        
        <pre>%s</pre>
        """;
    private final InviteService inviteService;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final Invite invite = inviteService.createNewInvite(context.getUser().getId());

        final SendMessage message = SendMessage.builder()
            .chatId(context.getChatId())
            .text(INVITE_CODE_GENERATED_MESSAGE.formatted(invite.getCode()))
            .parseMode("HTML")
            .build();
        return Optional.of(message);
    }

    @Override
    public List<String> getSupportedCallbacks() {
        return List.of(TelegramCallbackAction.CREATE_INVITE_CODE.name());
    }
}
