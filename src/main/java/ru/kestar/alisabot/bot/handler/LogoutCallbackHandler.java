package ru.kestar.alisabot.bot.handler;

import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.LOGOUT;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.kestar.alisabot.bot.menu.MenuBuilder;
import ru.kestar.alisabot.service.BotUserService;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class LogoutCallbackHandler implements UpdateHandler {
    private final BotUserService botUserService;
    private final MenuBuilder menuBuilder;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final String chatId = context.getChatId();
        botUserService.logoutUser(chatId);

        final EditMessageText responseMessage = EditMessageText.builder()
            .chatId(chatId)
            .messageId(context.getCallbackData().getMessageId())
            .text("Вы вышли из аккаунта. Для выполнения действий необходимо авторизоваться.")
            .replyMarkup(menuBuilder.buildUnauthenticatedUserStartMenu(chatId))
            .build();
        return Optional.of(responseMessage);
    }

    @Override
    public List<String> getSupportedCallbacks() {
        return List.of(LOGOUT.name());
    }
}
