package org.example.iniconfig.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IniConfigDto {
    @JsonProperty("ServerSettings")
    ServerSettings serverSettings;
    @JsonProperty("MetricSettings")
    MetricSettings metricSettings;
}
