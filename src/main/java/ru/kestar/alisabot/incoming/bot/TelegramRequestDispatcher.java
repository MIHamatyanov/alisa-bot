package ru.kestar.alisabot.incoming.bot;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kestar.alisabot.exception.UnknownBotCommandException;
import ru.kestar.alisabot.incoming.bot.handler.GetTokenCallbackHandler;
import ru.kestar.alisabot.incoming.bot.handler.LogoutCallbackHandler;
import ru.kestar.alisabot.incoming.bot.handler.StartCommandHandler;
import ru.kestar.alisabot.incoming.bot.handler.UpdateHandler;
import ru.kestar.alisabot.security.SecurityContextHelper;

@Component
@RequiredArgsConstructor
public class TelegramRequestDispatcher {
    private final GetTokenCallbackHandler getTokenCallbackHandler;
    private final LogoutCallbackHandler logoutCallbackHandler;
    private final StartCommandHandler startCommandHandler;

    public Optional<BotApiMethod<?>> dispatch(Update update) {
        try {
            if (update.hasMessage()) {
                return dispatchMessage(update);
            }
            if (update.hasCallbackQuery()) {
                return dispatchCallback(update);
            }
            throw new UnknownBotCommandException();
        } catch (UnknownBotCommandException e) {
            final SendMessage responseMessage = SendMessage.builder()
                .chatId(SecurityContextHelper.getActiveUser().getChatId())
                .text("Неизвестное действие. Для старта нажмите /start")
                .build();
            return Optional.of(responseMessage);
        }
    }

    private Optional<BotApiMethod<?>> dispatchMessage(Update update) {
        final String messageText = update.getMessage().getText();
        if (messageText.startsWith("/start")) {
            return startCommandHandler.handle(update);
        }
        throw new UnknownBotCommandException();
    }

    private Optional<BotApiMethod<?>> dispatchCallback(Update update) {
        final String callbackData = update.getCallbackQuery().getData();
        final UpdateHandler callbackHandler = switch (callbackData) {
            case "getToken" -> getTokenCallbackHandler;
            case "logout" -> logoutCallbackHandler;
            default -> throw new UnknownBotCommandException();
        };
        return callbackHandler.handle(update);
    }
}
