package ru.kestar.alisabot.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kestar.alisabot.model.enums.TelegramCallbackAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallbackData {

    @JsonProperty("action")
    private TelegramCallbackAction action;

    @JsonProperty("data")
    private Map<String, String> data;
}
