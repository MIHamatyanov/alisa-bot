package ru.kestar.alisabot.incoming.bot.handler;

import java.util.Optional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.kestar.alisabot.model.dto.TelegramActionContext;

public interface UpdateHandler {

    Optional<BotApiMethod<?>> handle(TelegramActionContext context);

}
