package ru.kestar.alisabot.bot.handler;

import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.START_MENU;
import static ru.kestar.alisabot.model.enums.TelegramCommand.START;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.kestar.alisabot.bot.menu.MenuBuilder;
import ru.kestar.alisabot.model.entity.BotUser;
import ru.kestar.alisabot.service.BotUserService;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {
    private static final String AUTHENTICATED_USER_START_MESSAGE = """
        Добро пожаловать!
        Выберите действие, которое хотите сделать.
        """;
    private static final String UNAUTHENTICATED_USER_START_MESSAGE = """
        Добро пожаловать!
        Для выполнения действий необходимо авторизоваться с помощью Яндекс.
        """;

    private final MenuBuilder menuBuilder;
    private final BotUserService botUserService;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final BotUser user = botUserService.getOrCreateUser(context.getUser());
        final boolean authenticated = user.isAuthenticated();

        final String messageText = authenticated
            ? AUTHENTICATED_USER_START_MESSAGE
            : UNAUTHENTICATED_USER_START_MESSAGE;

        final InlineKeyboardMarkup replyMarkup = authenticated
            ? menuBuilder.buildAuthenticatedUserStartMenu()
            : menuBuilder.buildUnauthenticatedUserStartMenu(user.getTelegramId());

        final BotApiMethod<?> responseMessage;
        if (context.getUpdate().hasCallbackQuery()) {
            responseMessage = EditMessageText.builder()
                .chatId(context.getChatId())
                .messageId(context.getCallbackData().getMessageId())
                .text(messageText)
                .replyMarkup(replyMarkup)
                .build();
        } else {
            responseMessage = SendMessage.builder()
                .chatId(context.getChatId())
                .text(messageText)
                .replyMarkup(replyMarkup)
                .build();
        }
        return Optional.of(responseMessage);
    }

    @Override
    public List<String> getSupportedCommands() {
        return List.of(START.getCommand());
    }

    @Override
    public List<String> getSupportedCallbacks() {
        return List.of(START_MENU.name());
    }
}
