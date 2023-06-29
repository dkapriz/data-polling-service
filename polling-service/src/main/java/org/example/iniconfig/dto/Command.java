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
public class Command {
    @JsonProperty("Metric_name")
    String metricName;
    @JsonProperty("Exec_command")
    String execCommand;
    @JsonProperty("Metric_pattern")
    String metricPattern;
    @JsonProperty("Interval")
    Integer interval;
}