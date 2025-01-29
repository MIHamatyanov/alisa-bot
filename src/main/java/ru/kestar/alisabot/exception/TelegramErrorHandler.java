package ru.kestar.alisabot.exception;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.exception.handler.ErrorHandler;
import ru.kestar.telegrambotstarter.exception.UnknownBotCommandException;

@Component
@RequiredArgsConstructor
public class TelegramErrorHandler {
    private final ErrorHandler errorHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        errorHandler.registerHandler(UnknownBotCommandException.class, this::handleUnknownCommandException);
        errorHandler.registerHandler(Exception.class, this::handleException);
    }

    private Optional<BotApiMethod<?>> handleUnknownCommandException(TelegramActionContext context,
                                                                    UnknownBotCommandException ex) {
        final SendMessage responseMessage = SendMessage.builder()
            .chatId(context.getChatId())
            .text("Неизвестное действие. Для старта нажмите /start")
            .build();
        return Optional.of(responseMessage);
    }

    private Optional<BotApiMethod<?>> handleException(TelegramActionContext context, Exception ex) {
        final SendMessage responseMessage = SendMessage.builder()
            .chatId(context.getChatId())
            .text("Произошла непредвиденная ошибка. Попробуйте позже.")
            .build();
        return Optional.of(responseMessage);
    }
}
