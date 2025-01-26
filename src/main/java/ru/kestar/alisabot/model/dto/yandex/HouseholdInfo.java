package ru.kestar.alisabot.model.dto.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HouseholdInfo {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;
}
