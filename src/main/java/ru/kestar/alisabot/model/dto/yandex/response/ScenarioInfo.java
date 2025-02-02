package ru.kestar.alisabot.model.dto.yandex.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScenarioInfo {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("is_active")
    private boolean active;
}
