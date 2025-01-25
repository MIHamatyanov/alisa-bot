package ru.kestar.alisabot.incoming.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kestar.alisabot.config.properties.TelegramBotProperties;
import ru.kestar.alisabot.model.dto.ActiveUserInfo;
import ru.kestar.alisabot.security.SecurityContextHelper;
import ru.kestar.alisabot.security.storage.TokenStorage;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    private final TelegramBotProperties botProperties;
    private final TokenStorage tokenStorage;
    private final TelegramRequestDispatcher requestDispatcher;

    public Bot(TelegramBotProperties botProperties, TokenStorage tokenStorage,
               TelegramRequestDispatcher requestDispatcher) {
        super(botProperties.getToken());

        this.botProperties = botProperties;
        this.tokenStorage = tokenStorage;
        this.requestDispatcher = requestDispatcher;
    }

    @Override
    public String getBotUsername() {
        return botProperties.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        fillSecurityContext(update);

        requestDispatcher.dispatch(update)
            .ifPresent(this::safeExecute);

        SecurityContextHelper.clear();
    }

    private void fillSecurityContext(Update update) {
        Message message = null;
        if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getMessage();
        } else if (update.hasMessage()) {
            message = update.getMessage();
        }

        ActiveUserInfo activeUserInfo = new ActiveUserInfo();
        if (message != null) {
            String chatId = message.getChatId().toString();
            activeUserInfo.setChatId(chatId);
            activeUserInfo.setTokenInfo(tokenStorage.get(chatId).orElse(null));
        }
        SecurityContextHelper.setActiveUser(activeUserInfo);
    }

    public void safeExecute(BotApiMethod<?> method) {
        try {
            execute(method);
        } catch (Exception e) {
            log.error("Error occurred while sending telegram response", e);
        }
    }
}
