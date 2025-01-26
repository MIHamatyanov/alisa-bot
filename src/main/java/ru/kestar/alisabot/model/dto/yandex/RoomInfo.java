package ru.kestar.alisabot.model.dto.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class RoomInfo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("devices")
    private List<String> devices;

    @JsonProperty("household_id")
    private String householdId;

}
