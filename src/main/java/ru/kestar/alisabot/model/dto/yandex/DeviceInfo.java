package ru.kestar.alisabot.model.dto.yandex;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class DeviceInfo {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("aliases")
    private List<String> aliases;

    @JsonProperty("room")
    private String room;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("skill_id")
    private String skillId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("groups")
    private List<String> groups;

    @JsonProperty("capabilities")
    private List<DeviceCapability> capabilities;

    @JsonProperty("properties")
    private List<DeviceProperty> properties;

    @JsonProperty("household_id")
    private String householdId;
}
