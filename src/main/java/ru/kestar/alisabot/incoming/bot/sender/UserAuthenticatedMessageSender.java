package ru.kestar.alisabot.incoming.bot.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.kestar.alisabot.incoming.bot.Bot;
import ru.kestar.alisabot.incoming.bot.menu.MenuBuilder;

@Component
@RequiredArgsConstructor
public class UserAuthenticatedMessageSender {
    private final Bot bot;
    private final MenuBuilder menuBuilder;

    public void send(String chatId, String login) {
        final SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text("""
                Атворизация прошла успешно.
                
                Добро пожаловать, %s!
                Что хотите сделать?
                """.formatted(login)
            )
            .replyMarkup(menuBuilder.buildAuthenticatedUserStartMenu())
            .build();

        bot.safeExecute(sendMessage);
    }
}
