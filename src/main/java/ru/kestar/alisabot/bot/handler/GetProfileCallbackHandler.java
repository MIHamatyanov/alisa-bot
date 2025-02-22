package ru.kestar.alisabot.bot.handler;

import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.GET_PROFILE;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.kestar.alisabot.bot.menu.MenuBuilder;
import ru.kestar.alisabot.model.entity.BotUser;
import ru.kestar.alisabot.service.BotUserService;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class GetProfileCallbackHandler implements UpdateHandler {
    private static final String PROFILE_MESSAGE_FORMAT = """
        Профиль:
        
        Логин - %s
        Токен - <pre>%s</pre>
        """;

    private final BotUserService botUserService;
    private final MenuBuilder menuBuilder;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final BotUser user = botUserService.getUserByTelegramId(context.getUser().getId());

        final EditMessageText.EditMessageTextBuilder responseBuilder = EditMessageText.builder()
            .chatId(context.getChatId())
            .messageId(context.getCallbackData().getMessageId());
        if (!user.isAuthenticated()) {
            responseBuilder.text("Вы не авторизованы. Нажмите на /start для авторизации.");
        } else {
            responseBuilder
                .text(PROFILE_MESSAGE_FORMAT.formatted(user.getLogin(), user.getToken()))
                .parseMode("HTML")
                .replyMarkup(menuBuilder.buildUserProfileMenu());
        }
        return Optional.of(responseBuilder.build());
    }

    @Override
    public List<String> getSupportedCallbacks() {
        return List.of(GET_PROFILE.name());
    }
}
