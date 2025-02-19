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
import ru.kestar.alisabot.exception.UserNotFoundException;
import ru.kestar.alisabot.model.entity.User;
import ru.kestar.alisabot.service.UserService;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {
    private final MenuBuilder menuBuilder;
    private final UserService userService;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        try {
            final User user = userService.getUserByTelegramId(context.getChatId());
            return user.isAuthenticated()
                ? buildAuthenticatedUserMessage(context, user)
                : buildUnauthenticatedUserMessage(context);
        } catch (UserNotFoundException e) {
            return buildUnauthenticatedUserMessage(context);
        }
    }

    private Optional<BotApiMethod<?>> buildAuthenticatedUserMessage(TelegramActionContext context,
                                                                    User user) {
        final String text = """
            Добро пожаловать, %s!
            Что хотите сделать?
            """.formatted(user.getLogin());
        final InlineKeyboardMarkup menu = menuBuilder.buildAuthenticatedUserStartMenu();

        final BotApiMethod<?> responseMessage;
        if (context.getUpdate().hasCallbackQuery()) {
            responseMessage = EditMessageText.builder()
                .chatId(context.getChatId())
                .messageId(context.getCallbackData().getMessageId())
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

    @Override
    public List<String> getSupportedCommands() {
        return List.of(START.getCommand());
    }

    @Override
    public List<String> getSupportedCallbacks() {
        return List.of(START_MENU.name());
    }
}
