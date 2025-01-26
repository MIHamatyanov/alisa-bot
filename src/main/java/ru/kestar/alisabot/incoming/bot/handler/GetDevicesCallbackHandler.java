package ru.kestar.alisabot.incoming.bot.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.kestar.alisabot.exception.YandexIntegrationException;
import ru.kestar.alisabot.incoming.bot.menu.MenuBuilder;
import ru.kestar.alisabot.model.dto.TelegramActionContext;
import ru.kestar.alisabot.model.dto.yandex.DeviceInfo;
import ru.kestar.alisabot.model.dto.yandex.SmartHouseInfo;
import ru.kestar.alisabot.service.YandexService;

@Component
@RequiredArgsConstructor
public class GetDevicesCallbackHandler implements UpdateHandler {
    private static final String DEVICES_MESSAGE_FORMAT = """
        Ваши устройства:
        ________________
        %s
        ________________
        """;
    private static final String DEVICE_MESSAGE_ROW_FORMAT = "%s) %s\n";

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

        final StringBuilder devicesInfo = new StringBuilder();
        for (int i = 0; i < smartHouseInfo.getDevices().size(); i++) {
            final DeviceInfo device = smartHouseInfo.getDevices().get(i);
            devicesInfo.append(DEVICE_MESSAGE_ROW_FORMAT.formatted(i + 1, device.getName()));
        }

        return EditMessageText.builder()
            .chatId(context.getChatId())
            .messageId(context.getCallbackMessageId())
            .text(DEVICES_MESSAGE_FORMAT.formatted(devicesInfo.toString()))
            .replyMarkup(menuBuilder.buildUserDevicesMenu())
            .build();
    }
}
