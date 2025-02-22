package ru.kestar.alisabot.bot.state;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import ru.kestar.alisabot.model.enums.TelegramState;

@Component
public class StateManager {
    private final Map<Long, TelegramState> userStates = new ConcurrentHashMap<>();

    public TelegramState getUserState(Long telegramId) {
        return userStates.get(telegramId);
    }

    public void changeState(Long telegramId, TelegramState state) {
        userStates.put(telegramId, state);
    }

    public void clearState(Long telegramId) {
        userStates.remove(telegramId);
    }
}
