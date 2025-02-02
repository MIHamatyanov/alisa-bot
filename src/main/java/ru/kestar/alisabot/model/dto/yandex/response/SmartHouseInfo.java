package ru.kestar.alisabot.model.dto.yandex.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class SmartHouseInfo {

    @JsonProperty("status")
    private String status;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("rooms")
    private List<RoomInfo> rooms;

    @JsonProperty("groups")
    private List<GroupInfo> groups;

    @JsonProperty("devices")
    private List<DeviceInfo> devices;

    @JsonProperty("scenarios")
    private List<ScenarioInfo> scenarios;

    @JsonProperty("households")
    private List<HouseholdInfo> households;
}
