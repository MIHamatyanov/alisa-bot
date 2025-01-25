package ru.kestar.alisabot.incoming.bot.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kestar.alisabot.model.dto.ActiveUserInfo;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.security.SecurityContextHelper;

@Component
@RequiredArgsConstructor
public class GetTokenCallbackHandler implements UpdateHandler {

    @Override
    public Optional<BotApiMethod<?>> handle(Update update) {
        final ActiveUserInfo activeUserInfo = SecurityContextHelper.getActiveUser();

        final String userToken = Optional.ofNullable(activeUserInfo.getTokenInfo())
            .map(YandexTokenInfo::getAccessToken)
            .map(token -> "Ваш токен: " + token)
            .orElse("Вы не авторизованы. Нажмите на /start для авторизации.");

        final SendMessage responseMessage = SendMessage.builder()
            .chatId(activeUserInfo.getChatId())
            .text(userToken)
            .build();
        return Optional.of(responseMessage);
    }
}
