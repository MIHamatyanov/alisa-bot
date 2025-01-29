package ru.kestar.alisabot.bot.menu;

import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.GET_HOUSE_INFO;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.GET_PROFILE;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.LOGOUT;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.START_MENU;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.kestar.alisabot.config.properties.ApplicationProperties;
import ru.kestar.alisabot.model.enums.TelegramCallbackAction;
import ru.kestar.telegrambotstarter.context.CallbackData;
import ru.kestar.telegrambotstarter.context.CallbackDataParser;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuBuilder {
    private final ApplicationProperties applicationProperties;
    private final CallbackDataParser callbackDataParser;

    public InlineKeyboardMarkup buildAuthenticatedUserStartMenu() {
        final InlineKeyboardButton getTokenBtn = InlineKeyboardButton.builder()
            .text("Профиль")
            .callbackData(createCallbackData(GET_PROFILE))
            .build();

        final InlineKeyboardButton getHouseInfo = InlineKeyboardButton.builder()
            .text("Получить информацию об устройствах")
            .callbackData(createCallbackData(GET_HOUSE_INFO))
            .build();

        final InlineKeyboardButton logoutBtn = InlineKeyboardButton.builder()
            .text("Выйти из аккаунта")
            .callbackData(createCallbackData(LOGOUT))
            .build();

        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(getTokenBtn))
            .keyboardRow(List.of(getHouseInfo))
            .keyboardRow(List.of(logoutBtn))
            .build();
    }

    public InlineKeyboardMarkup buildUnauthenticatedUserStartMenu(String chatId) {
        final InlineKeyboardButton authorizeButton = InlineKeyboardButton.builder()
            .text("Авторизоваться")
            .url(applicationProperties.getBaseUrl() + "/oauth2/authorization/yandex?telegramId=" + chatId)
            .build();

        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(authorizeButton))
            .build();
    }

    public InlineKeyboardMarkup buildUserDevicesMenu() {
        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(createBackToStartMenuBtn()))
            .build();
    }

    public InlineKeyboardMarkup buildUserProfileMenu() {
        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(createBackToStartMenuBtn()))
            .build();
    }

    private InlineKeyboardButton createBackToStartMenuBtn() {
        return InlineKeyboardButton.builder()
            .text("<- Назад")
            .callbackData(createCallbackData(START_MENU))
            .build();
    }

    private String createCallbackData(TelegramCallbackAction action) {
        return createCallbackData(action, null);
    }

    private String createCallbackData(TelegramCallbackAction action, Map<String, String> data) {
        final CallbackData callbackData = new CallbackData(action.name(), data);
        return callbackDataParser.toString(callbackData);
    }
}
