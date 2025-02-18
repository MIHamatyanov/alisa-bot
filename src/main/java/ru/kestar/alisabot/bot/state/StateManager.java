package ru.kestar.alisabot.bot.state;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import ru.kestar.alisabot.model.enums.TelegramState;

@Component
public class StateManager {
    private final Map<String, TelegramState> userStates = new ConcurrentHashMap<>();

    public TelegramState getUserState(String telegramId) {
        return userStates.get(telegramId);
    }

    public void changeState(String telegramId, TelegramState state) {
        userStates.put(telegramId, state);
    }

    public void clearState(String telegramId) {
        userStates.remove(telegramId);
    }
}
