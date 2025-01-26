package ru.kestar.alisabot.incoming.bot.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.kestar.alisabot.incoming.bot.menu.MenuBuilder;
import ru.kestar.alisabot.model.dto.TelegramActionContext;
import ru.kestar.alisabot.model.dto.YandexTokenInfo;
import ru.kestar.alisabot.security.storage.TokenStorage;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {
    private final MenuBuilder menuBuilder;
    private final TokenStorage tokenStorage;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final Optional<YandexTokenInfo> tokenInfo = tokenStorage.get(context.getChatId());
        return tokenInfo.isPresent()
            ? buildAuthenticatedUserMessage(context, tokenInfo.get())
            : buildUnauthenticatedUserMessage(context);
    }

    private Optional<BotApiMethod<?>> buildAuthenticatedUserMessage(TelegramActionContext context,
                                                                    YandexTokenInfo tokenInfo) {
        final String text = """
            Добро пожаловать, %s!
            Что хотите сделать?
            """.formatted(tokenInfo.getLogin());
        final InlineKeyboardMarkup menu = menuBuilder.buildAuthenticatedUserStartMenu();

        final BotApiMethod<?> responseMessage;
        if (context.getUpdate().hasCallbackQuery()) {
            responseMessage = EditMessageText.builder()
                .chatId(context.getChatId())
                .messageId(context.getCallbackMessageId())
                .text(text)
                .replyMarkup(menu)
                .build();
        } else {
            responseMessage = SendMessage.builder()
                .chatId(context.getChatId())
                .text(text)
                .replyMarkup(menu)
                .build();
        }
        return Optional.of(responseMessage);
    }

    private Optional<BotApiMethod<?>> buildUnauthenticatedUserMessage(TelegramActionContext context) {
        final SendMessage responseMessage = SendMessage.builder()
            .chatId(context.getChatId())
            .text("Добро пожаловать! Для начала работы авторизуйтесь.")
            .replyMarkup(menuBuilder.buildUnauthenticatedUserStartMenu(context.getChatId()))
            .build();
        return Optional.of(responseMessage);
    }
}
