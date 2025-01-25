package ru.kestar.alisabot.incoming.bot.handler;

import java.util.Optional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {

    Optional<BotApiMethod<?>> handle(Update update);

}
