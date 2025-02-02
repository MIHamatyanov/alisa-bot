package ru.kestar.alisabot.model.dto.yandex.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionInfo {
    @JsonProperty("type")
    private String type;

    @JsonProperty("state")
    private Map<String, Object> state;
}
