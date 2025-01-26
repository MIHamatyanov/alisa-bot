package ru.kestar.alisabot.incoming.bot.handler;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.kestar.alisabot.model.dto.TelegramActionContext;
import ru.kestar.alisabot.model.enums.TelegramCallbackAction;
import ru.kestar.alisabot.model.enums.TelegramCommand;

public interface UpdateHandler {

    Optional<BotApiMethod<?>> handle(TelegramActionContext context);

    default List<TelegramCommand> getSupportedCommands() {
        return Collections.emptyList();
    }

    default List<TelegramCallbackAction> getSupportedCallbacks() {
        return Collections.emptyList();
    }
}
