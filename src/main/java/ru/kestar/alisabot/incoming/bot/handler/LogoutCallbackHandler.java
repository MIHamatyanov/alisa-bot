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
import ru.kestar.alisabot.security.storage.TokenStorage;

@Component
@RequiredArgsConstructor
public class LogoutCallbackHandler implements UpdateHandler {
    private final TokenStorage tokenStorage;
    private final MenuBuilder menuBuilder;

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        final ActiveUserInfo activeUserInfo = SecurityContextHelper.getActiveUser();
        tokenStorage.revokeToken(activeUserInfo.getChatId());

        final SendMessage responseMessage = SendMessage.builder()
            .chatId(activeUserInfo.getChatId())
            .text("Вы вышли из аккаунта. Для выполнения действий необходимо авторизоваться.")
            .replyMarkup(menuBuilder.buildUnauthenticatedUserStartMenu())
            .build();
        return Optional.of(responseMessage);
    }
}
