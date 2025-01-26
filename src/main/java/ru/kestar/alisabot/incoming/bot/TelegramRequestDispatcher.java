package ru.kestar.alisabot.incoming.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kestar.alisabot.exception.InternalUnexpectedException;
import ru.kestar.alisabot.exception.UnknownBotCommandException;
import ru.kestar.alisabot.incoming.bot.handler.GetDevicesCallbackHandler;
import ru.kestar.alisabot.incoming.bot.handler.GetTokenCallbackHandler;
import ru.kestar.alisabot.incoming.bot.handler.LogoutCallbackHandler;
import ru.kestar.alisabot.incoming.bot.handler.StartCommandHandler;
import ru.kestar.alisabot.incoming.bot.handler.UpdateHandler;
import ru.kestar.alisabot.model.dto.CallbackData;
import ru.kestar.alisabot.model.dto.TelegramActionContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramRequestDispatcher {
    private final GetTokenCallbackHandler getTokenCallbackHandler;
    private final GetDevicesCallbackHandler getDevicesCallbackHandler;
    private final LogoutCallbackHandler logoutCallbackHandler;
    private final StartCommandHandler startCommandHandler;
    private final ObjectMapper objectMapper;

    public Optional<BotApiMethod<?>> dispatch(TelegramActionContext context) {
        try {
            final Update update = context.getUpdate();
            if (update.hasMessage()) {
                return dispatchMessage(context);
            }
            if (update.hasCallbackQuery()) {
                return dispatchCallback(context);
            }
            throw new UnknownBotCommandException();
        } catch (UnknownBotCommandException e) {
            final SendMessage responseMessage = SendMessage.builder()
                .chatId(context.getChatId())
                .text("Неизвестное действие. Для старта нажмите /start")
                .build();
            return Optional.of(responseMessage);
        } catch (InternalUnexpectedException e) {
            final SendMessage responseMessage = SendMessage.builder()
                .chatId(context.getChatId())
                .text("Произошла непредвиденная ошибка. Попробуйте позже.")
                .build();
            return Optional.of(responseMessage);
        }
    }

    private Optional<BotApiMethod<?>> dispatchMessage(TelegramActionContext context) {
        final String messageText = context.getUpdate().getMessage().getText();
        if (messageText.startsWith("/start")) {
            return startCommandHandler.handle(context);
        }
        throw new UnknownBotCommandException();
    }

    private Optional<BotApiMethod<?>> dispatchCallback(TelegramActionContext context) {
        final CallbackData callbackData = getCallbackData(context.getUpdate().getCallbackQuery());
        final UpdateHandler callbackHandler = switch (callbackData.getAction()) {
            case GET_TOKEN -> getTokenCallbackHandler;
            case GET_HOUSE_INFO -> getDevicesCallbackHandler;
            case LOGOUT -> logoutCallbackHandler;
            case START_MENU -> startCommandHandler;
        };
        return callbackHandler.handle(context);
    }

    private CallbackData getCallbackData(CallbackQuery callbackQuery) {
        try {
            return objectMapper.readValue(callbackQuery.getData(), CallbackData.class);
        } catch (Exception e) {
            log.error("Error while parse callback query", e);
            throw new InternalUnexpectedException(e.getMessage());
        }
    }
}
