package ru.kestar.alisabot.incoming.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kestar.alisabot.exception.InternalUnexpectedException;
import ru.kestar.alisabot.exception.UnknownBotCommandException;
import ru.kestar.alisabot.incoming.bot.handler.UpdateHandler;
import ru.kestar.alisabot.model.dto.CallbackData;
import ru.kestar.alisabot.model.dto.TelegramActionContext;
import ru.kestar.alisabot.model.enums.TelegramCallbackAction;
import ru.kestar.alisabot.model.enums.TelegramCommand;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramRequestDispatcher {
    private final ObjectMapper objectMapper;

    private final Map<TelegramCommand, UpdateHandler> commandHandlers = new EnumMap<>(TelegramCommand.class);
    private final Map<TelegramCallbackAction, UpdateHandler> callbackHandlers = new EnumMap<>(TelegramCallbackAction.class);

    public void registerCommandHandler(TelegramCommand command, UpdateHandler handler) {
        commandHandlers.put(command, handler);
    }

    public void registerCallbackHandler(TelegramCallbackAction action, UpdateHandler handler) {
        callbackHandlers.put(action, handler);
    }

    public Optional<BotApiMethod<?>> dispatch(TelegramActionContext context) {
        try {
            final Update update = context.getUpdate();
            if (update.hasMessage()) {
                return dispatchCommand(context);
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

    private Optional<BotApiMethod<?>> dispatchCommand(TelegramActionContext context) {
        final Message message = context.getUpdate().getMessage();
        if (message.isCommand()) {
            final String messageFirstWord = message.getText().split(" ")[0];
            final TelegramCommand command = TelegramCommand.fromString(messageFirstWord);
            if (command != null && commandHandlers.containsKey(command)) {
                return commandHandlers.get(command).handle(context);
            }
        }
        throw new UnknownBotCommandException();
    }

    private Optional<BotApiMethod<?>> dispatchCallback(TelegramActionContext context) {
        final CallbackQuery callbackQuery = context.getUpdate().getCallbackQuery();
        final CallbackData callbackData = getCallbackData(callbackQuery);
        context.setCallbackData(callbackData);
        context.setCallbackMessageId(callbackQuery.getMessage().getMessageId());

        if (callbackData != null && callbackData.getAction() != null && callbackHandlers.containsKey(callbackData.getAction())) {
            return callbackHandlers.get(callbackData.getAction()).handle(context);
        }
        throw new UnknownBotCommandException();
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
