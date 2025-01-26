package ru.kestar.alisabot.incoming.bot.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.kestar.alisabot.model.dto.TelegramActionContext;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.security.storage.TokenStorage;

@Component
@RequiredArgsConstructor
public class GetTokenCallbackHandler implements UpdateHandler {
    private final TokenStorage tokenStorage;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final String userToken = tokenStorage.get(context.getChatId())
            .map(YandexTokenInfo::getAccessToken)
            .map(token -> "Ваш токен: " + token)
            .orElse("Вы не авторизованы. Нажмите на /start для авторизации.");

        final SendMessage responseMessage = SendMessage.builder()
            .chatId(context.getChatId())
            .text(userToken)
            .build();
        return Optional.of(responseMessage);
    }
}
