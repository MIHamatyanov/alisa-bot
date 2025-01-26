package ru.kestar.alisabot.model.dto;

import java.util.Optional;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
public class TelegramActionContext {
    private Update update;
    private CallbackData callbackData;
    private Integer callbackMessageId;

    public TelegramActionContext(Update update) {
        this.update = update;
    }

    public String getChatId() {
        Message message = null;
        if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getMessage();
        } else if (update.hasMessage()) {
            message = update.getMessage();
        }

        return Optional.ofNullable(message)
            .map(Message::getChatId)
            .map(Object::toString)
            .orElse("");
    }
}
