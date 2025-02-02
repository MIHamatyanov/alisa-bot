package ru.kestar.alisabot.bot.handler;

import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.TURN_OFF;
import static ru.kestar.alisabot.model.enums.TelegramCallbackAction.TURN_ON;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.kestar.alisabot.service.YandexService;
import ru.kestar.telegrambotstarter.context.CallbackData;
import ru.kestar.telegrambotstarter.context.TelegramActionContext;
import ru.kestar.telegrambotstarter.handler.UpdateHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ToggleDeviceCallbackHandler implements UpdateHandler {
    private final YandexService yandexService;

    @Override
    public Optional<BotApiMethod<?>> handle(TelegramActionContext context) {
        final CallbackData callbackData = context.getCallbackData();
        final String[] dataParts = callbackData.getData().split(";");
        final String target = dataParts[0];
        final String targetId = dataParts[1];

        log.info("Executing action for {} with id {}", target, targetId);
        if ("group".equals(target)) {
            yandexService.executeGroupAction(context.getChatId(), targetId, callbackData.getAction().equals(TURN_ON.name()));
        } else if ("device".equals(target)) {
            yandexService.executeDeviceAction(context.getChatId(), targetId, callbackData.getAction().equals(TURN_ON.name()));
        }
        return Optional.empty();
    }

    @Override
    public List<String> getSupportedCallbacks() {
        return List.of(TURN_OFF.name(), TURN_ON.name());
    }
}
