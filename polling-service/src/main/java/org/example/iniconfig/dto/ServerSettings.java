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
public class ServerSettings {
    @JsonProperty("Collector")
    String collector;
    @JsonProperty("Port")
    String port;
    @JsonProperty("Interval")
    Integer interval;
    @JsonProperty("Console_encoding")
    String consoleEncoding;
}
