package ru.kestar.alisabot.model.dto.yandex.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceActionsInfo {
    @JsonProperty("id")
    private String id;

    @JsonProperty("actions")
    private List<ActionInfo> actions;
}
