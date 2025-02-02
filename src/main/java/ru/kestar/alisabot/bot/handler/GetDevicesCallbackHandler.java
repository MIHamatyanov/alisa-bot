package ru.kestar.alisabot.bot.handler;

import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.GET_HOUSE_INFO;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.kestar.alisabot.bot.menu.MenuBuilder;
import ru.kestar.alisabot.exception.YandexIntegrationException;
import ru.kestar.alisabot.model.dto.yandex.response.DeviceInfo;
import ru.kestar.alisabot.model.dto.yandex.response.GroupInfo;
import ru.kestar.alisabot.model.dto.yandex.response.SmartHouseInfo;
import ru.kestar.alisabot.service.YandexService;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.UpdateHandler;

@Component
@RequiredArgsConstructor
public class GetDevicesCallbackHandler implements UpdateHandler {
    private static final String DEVICES_MESSAGE_FORMAT = """
        Группы устройств:
        ________________
        %s
        ________________
        
        Устройства:
        ________________
        %s
        ________________
        """;
    private static final String DEVICE_MESSAGE_ROW_FORMAT = "%s) %s";

    private final YandexService yandexService;
    private final MenuBuilder menuBuilder;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        try {
            return Optional.ofNullable(getSmartHouseInfo(context));
        } catch (YandexIntegrationException e) {
            final SendMessage errorMessage = SendMessage.builder()
                .chatId(context.getChatId())
                .text("Не удалось получить информацию об устройствах. Пожалуйста, попробуйте позже.")
                .build();
            return Optional.of(errorMessage);
        }
    }

    private BotApiMethod<?> getSmartHouseInfo(TelegramActionContext context) {
        final SmartHouseInfo smartHouseInfo = yandexService.getSmartHouseInfo(context.getChatId());

        final StringJoiner groupsInfo = new StringJoiner("\n");
        for (int i = 0; i < smartHouseInfo.getGroups().size(); i++) {
            final GroupInfo groupInfo = smartHouseInfo.getGroups().get(i);
            groupsInfo.add(DEVICE_MESSAGE_ROW_FORMAT.formatted(i + 1, groupInfo.getName()));
        }

        final StringJoiner devicesInfo = new StringJoiner("\n");
        for (int i = 0; i < smartHouseInfo.getDevices().size(); i++) {
            final DeviceInfo device = smartHouseInfo.getDevices().get(i);
            devicesInfo.add(DEVICE_MESSAGE_ROW_FORMAT.formatted(i + 1, device.getName()));
        }

        return EditMessageText.builder()
            .chatId(context.getChatId())
            .messageId(context.getCallbackData().getMessageId())
            .text(DEVICES_MESSAGE_FORMAT.formatted(groupsInfo.toString(), devicesInfo.toString()))
            .replyMarkup(menuBuilder.buildUserDevicesMenu(smartHouseInfo))
            .build();
    }

    @Override
    public List<String> getSupportedCallbacks() {
        return List.of(GET_HOUSE_INFO.name());
    }
}
