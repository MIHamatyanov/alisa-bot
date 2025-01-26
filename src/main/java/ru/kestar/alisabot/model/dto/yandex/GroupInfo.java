package ru.kestar.alisabot.model.dto.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class GroupInfo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("devices")
    private List<String> devices;

    @JsonProperty("household_id")
    private String householdId;

    @JsonProperty("aliases")
    private List<String> aliases;

    @JsonProperty("type")
    private String type;

    @JsonProperty("capabilities")
    private List<GroupCapability> capabilities;
}
