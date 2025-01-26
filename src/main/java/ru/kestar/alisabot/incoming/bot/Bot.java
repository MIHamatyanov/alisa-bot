package ru.kestar.alisabot.incoming.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kestar.alisabot.config.properties.TelegramBotProperties;
import ru.kestar.alisabot.model.dto.TelegramActionContext;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    private final TelegramBotProperties botProperties;
    private final TelegramRequestDispatcher requestDispatcher;

    public Bot(TelegramBotProperties botProperties,
               TelegramRequestDispatcher requestDispatcher) {
        super(botProperties.getToken());

        this.botProperties = botProperties;
        this.requestDispatcher = requestDispatcher;
    }

    @Override
    public String getBotUsername() {
        return botProperties.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        final TelegramActionContext context = new TelegramActionContext(update);
        requestDispatcher.dispatch(context)
            .ifPresent(this::safeExecute);
    }

    public void safeExecute(BotApiMethod<?> method) {
        try {
            execute(method);
        } catch (Exception e) {
            log.error("Error occurred while sending telegram response", e);
        }
    }
}
