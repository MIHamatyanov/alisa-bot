package ru.kestar.alisabot.model.dto.yandex.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteDeviceActionsRequest {
    @JsonProperty("devices")
    private List<DeviceActionsInfo> devices;
}
