package ru.kestar.alisabot.bot.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.kestar.alisabot.bot.menu.MenuBuilder;
import ru.kestar.telegrambotstarter.bot.TelegramBot;

@Component
@RequiredArgsConstructor
public class UserAuthenticatedMessageSender {
    private static final String SUCCESS_AUTHENTICATION_MESSAGE = """
        Авторизация прошла успешно.
        
        Выберите действие.
        """;

    private final TelegramBot bot;
    private final MenuBuilder menuBuilder;

    public void send(String chatId) {
        final SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text(SUCCESS_AUTHENTICATION_MESSAGE)
            .replyMarkup(menuBuilder.buildAuthenticatedUserStartMenu())
            .build();

        bot.safeExecute(sendMessage);
    }
}
