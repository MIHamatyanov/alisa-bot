package ru.kestar.alisabot.bot.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.kestar.alisabot.bot.state.StateManager;
import ru.kestar.alisabot.model.enums.TelegramState;
import ru.kestar.alisabot.service.InviteService;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.DefaultMessageHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class TextMessageHandler extends DefaultMessageHandler {
    private static final String INVITE_CODE_DELETED_MESSAGE = "Код приглашения удален";
    private static final String INVITE_CODE_NOT_DELETED_MESSAGE = "Код приглашения не найден";

    private final StateManager stateManager;
    private final InviteService inviteService;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final TelegramState currentState = stateManager.getUserState(context.getUser().getId());

        return switch (currentState) {
            case DELETE_INVITE_CODE -> handleDeleteInviteCode(context);
        };
    }

    private Optional<BotApiMethod<?>> handleDeleteInviteCode(TelegramActionContext context) {
        boolean codeDeleted = inviteService.removeInviteCode(context.getUser().getId(), context.getUpdate().getMessage().getText());
        stateManager.clearState(context.getUser().getId());

        final SendMessage response = SendMessage.builder()
            .chatId(context.getChatId())
            .text(codeDeleted ? INVITE_CODE_DELETED_MESSAGE : INVITE_CODE_NOT_DELETED_MESSAGE)
            .build();
        return Optional.of(response);
    }
}
