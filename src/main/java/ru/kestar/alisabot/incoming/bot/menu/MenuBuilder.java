package ru.kestar.alisabot.incoming.bot.menu;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.kestar.alisabot.config.properties.ApplicationProperties;
import ru.kestar.alisabot.security.SecurityContextHelper;

@Component
@RequiredArgsConstructor
public class MenuBuilder {
    private final ApplicationProperties applicationProperties;

    public InlineKeyboardMarkup buildAuthenticatedUserStartMenu() {
        final InlineKeyboardButton getTokenBtn = InlineKeyboardButton.builder()
            .text("Получить токен")
            .callbackData("getToken")
            .build();

        final InlineKeyboardButton logoutBtn = InlineKeyboardButton.builder()
            .text("Выйти из аккаунта")
            .callbackData("logout")
            .build();

        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(getTokenBtn))
            .keyboardRow(List.of(logoutBtn))
            .build();
    }

    public InlineKeyboardMarkup buildUnauthenticatedUserStartMenu() {
        final InlineKeyboardButton authorizeButton = InlineKeyboardButton.builder()
            .text("Авторизоваться")
            .url(applicationProperties.getBaseUrl() + "/oauth2/authorization/yandex?telegramId=" + SecurityContextHelper.getActiveUser().getChatId())
            .build();

        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(authorizeButton))
            .build();
    }
}
