package ru.kestar.alisabot.incoming.bot.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kestar.alisabot.incoming.bot.menu.MenuBuilder;
import ru.kestar.alisabot.model.dto.ActiveUserInfo;
import ru.kestar.alisabot.security.SecurityContextHelper;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {
    private final MenuBuilder menuBuilder;

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        final ActiveUserInfo activeUserInfo = SecurityContextHelper.getActiveUser();

        return activeUserInfo.isAuthenticated()
            ? buildAuthenticatedUserMessage(activeUserInfo)
            : buildUnauthenticatedUserMessage(activeUserInfo);
    }

    private Optional<BotApiMethod<?>> buildAuthenticatedUserMessage(ActiveUserInfo activeUserInfo) {
        final SendMessage responseMessage = SendMessage.builder()
            .chatId(activeUserInfo.getChatId())
            .text("""
                Добро пожаловать, %s!
                Что хотите сделать?
                """.formatted(activeUserInfo.getTokenInfo().getLogin())
            )
            .replyMarkup(menuBuilder.buildAuthenticatedUserStartMenu())
            .build();
        return Optional.of(responseMessage);
    }

    private Optional<BotApiMethod<?>> buildUnauthenticatedUserMessage(ActiveUserInfo activeUserInfo) {
        final SendMessage responseMessage = SendMessage.builder()
            .chatId(activeUserInfo.getChatId())
            .text("Добро пожаловать! Для начала работы авторизуйтесь.")
            .replyMarkup(menuBuilder.buildUnauthenticatedUserStartMenu())
            .build();
        return Optional.of(responseMessage);
    }
}
