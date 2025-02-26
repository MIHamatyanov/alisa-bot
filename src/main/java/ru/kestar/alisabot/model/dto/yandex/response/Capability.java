package ru.kestar.alisabot.model.dto.yandex.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

@Data
public class Capability {
    @JsonProperty("type")
    private String type;

    @JsonProperty("retrievable")
    private boolean retrievable;

    @JsonProperty("parameters")
    private Map<String, Object> parameters;

    @JsonProperty("state")
    private Map<String, Object> state;
}
