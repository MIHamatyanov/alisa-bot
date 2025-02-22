package ru.kestar.alisabot.bot.menu;

import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.CREATE_INVITE_CODE;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.DELETE_INVITE_CODE;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.GET_HOUSE_INFO;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.GET_PROFILE;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.LOGOUT;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.START_MENU;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.TURN_OFF;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.TURN_ON;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.SHOW_INVITE_CODE;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.kestar.alisabot.config.properties.ApplicationProperties;
import ru.kestar.alisabot.model.dto.yandex.response.SmartHouseInfo;
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

    public InlineKeyboardMarkup buildUnauthenticatedUserStartMenu(Long telegramUserId) {
        final InlineKeyboardButton authorizeButton = InlineKeyboardButton.builder()
            .text("Авторизоваться")
            .url(applicationProperties.getBaseUrl() + "/oauth2/authorization/yandex?telegramId=" + telegramUserId)
            .build();

        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(authorizeButton))
            .build();
    }

    public InlineKeyboardMarkup buildUserDevicesMenu(SmartHouseInfo smartHouseInfo) {
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        smartHouseInfo.getGroups().forEach(group -> {
            final InlineKeyboardButton turnOnGroupBtn = InlineKeyboardButton.builder()
                .text("Включить " + group.getName())
                .callbackData(createCallbackData(TURN_ON, "group;" + group.getId()))
                .build();

            final InlineKeyboardButton turnOffGroupBtn = InlineKeyboardButton.builder()
                .text("Выключить " + group.getName())
                .callbackData(createCallbackData(TURN_OFF, "group;" + group.getId()))
                .build();

            keyboardRows.add(List.of(turnOnGroupBtn, turnOffGroupBtn));
        });

        smartHouseInfo.getDevices().forEach(device -> {
            final InlineKeyboardButton turnOnGroupBtn = InlineKeyboardButton.builder()
                .text("Включить " + device.getName())
                .callbackData(createCallbackData(TURN_ON, "device;" + device.getId()))
                .build();

            final InlineKeyboardButton turnOffGroupBtn = InlineKeyboardButton.builder()
                .text("Выключить " + device.getName())
                .callbackData(createCallbackData(TURN_OFF, "device;" + device.getId()))
                .build();

            keyboardRows.add(List.of(turnOnGroupBtn, turnOffGroupBtn));
        });

        return InlineKeyboardMarkup.builder()
            .keyboard(keyboardRows)
            .keyboardRow(List.of(createBackToStartMenuBtn()))
            .build();
    }

    public InlineKeyboardMarkup buildUserProfileMenu() {
        final InlineKeyboardButton createInviteCodeBtn = InlineKeyboardButton.builder()
            .text("Создать код приглашения")
            .callbackData(createCallbackData(CREATE_INVITE_CODE))
            .build();
        final InlineKeyboardButton viewInviteCodeBtn = InlineKeyboardButton.builder()
            .text("Посмотреть коды приглашения")
            .callbackData(createCallbackData(SHOW_INVITE_CODE))
            .build();

        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(createInviteCodeBtn, viewInviteCodeBtn))
            .keyboardRow(List.of(createBackToStartMenuBtn()))
            .build();
    }

    public InlineKeyboardMarkup buildInviteCodesMenu() {
        final InlineKeyboardButton deleteInviteCodeBtn = InlineKeyboardButton.builder()
            .text("Удалить код приглашения")
            .callbackData(createCallbackData(DELETE_INVITE_CODE))
            .build();
        return InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(deleteInviteCodeBtn))
            .keyboardRow(List.of(createBackToUserProfileBtn()))
            .build();
    }

    private InlineKeyboardButton createBackToStartMenuBtn() {
        return InlineKeyboardButton.builder()
            .text("<- Назад")
            .callbackData(createCallbackData(START_MENU))
            .build();
    }

    private InlineKeyboardButton createBackToUserProfileBtn() {
        return InlineKeyboardButton.builder()
            .text("<- Назад")
            .callbackData(createCallbackData(GET_PROFILE))
            .build();
    }

    private String createCallbackData(TelegramCallbackAction action) {
        return createCallbackData(action, null);
    }

    private String createCallbackData(TelegramCallbackAction action, String data) {
        final CallbackData callbackData = new CallbackData(action.name(), data);
        return callbackDataParser.toString(callbackData);
    }
}
