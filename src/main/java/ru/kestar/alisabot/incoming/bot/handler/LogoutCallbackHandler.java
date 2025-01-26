package ru.kestar.alisabot.incoming.bot.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.kestar.alisabot.incoming.bot.menu.MenuBuilder;
import ru.kestar.alisabot.model.dto.TelegramActionContext;
import ru.kestar.alisabot.security.storage.TokenStorage;

@Component
@RequiredArgsConstructor
public class LogoutCallbackHandler implements UpdateHandler {
    private final TokenStorage tokenStorage;
    private final MenuBuilder menuBuilder;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final String chatId = context.getChatId();
        tokenStorage.revokeToken(chatId);

        final EditMessageText responseMessage = EditMessageText.builder()
            .chatId(chatId)
            .messageId(context.getUpdate().getCallbackQuery().getMessage().getMessageId())
            .text("Вы вышли из аккаунта. Для выполнения действий необходимо авторизоваться.")
            .replyMarkup(menuBuilder.buildUnauthenticatedUserStartMenu(chatId))
            .build();
        return Optional.of(responseMessage);
    }
}
