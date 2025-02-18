package ru.kestar.alisabot.bot.handler;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.kestar.alisabot.bot.state.StateManager;
import ru.kestar.alisabot.model.enums.TelegramCallbackAction;
import ru.kestar.alisabot.model.enums.TelegramState;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.UpdateHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteInviteCodeCallbackHandler implements UpdateHandler {
    private static final String DELETE_CODE_MESSAGE = "Введите код который хотите удалить";

    private final StateManager stateManager;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        stateManager.changeState(context.getChatId(), TelegramState.DELETE_INVITE_CODE);

        final SendMessage response = SendMessage.builder()
            .chatId(context.getChatId())
            .text(DELETE_CODE_MESSAGE)
            .build();
        return Optional.of(response);
    }

    @Override
    public List<String> getSupportedCallbacks() {
        return List.of(TelegramCallbackAction.DELETE_INVITE_CODE.name());
    }
}
