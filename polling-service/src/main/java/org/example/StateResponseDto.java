package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StateResponseDto {
    String hostname;
    String timestamp;
    @JsonProperty("metric_name")
    String metricName;
    @JsonProperty("metric_value")
    String metricValue;
    @JsonProperty("execution_error")
    String executionError;

    public static class StateResponseDtoBuilder {
        public StateResponseDtoBuilder initDefaultField() {
            this.timestamp = String.valueOf(System.currentTimeMillis());
            try {
                this.hostname = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                this.hostname = "unknown name";
            }
            return this;
        }
    }

    public String toString() {
        return "{" + System.lineSeparator() +
                "“hostname ”: " + hostname + ", " + System.lineSeparator() +
                "“timestamp”: " + timestamp + ", " + System.lineSeparator() +
                "“metric_name”: " + metricName + ", " + System.lineSeparator() +
                "”metric_value”: " + metricValue + ", " + System.lineSeparator() +
                "”execution_error”: " + executionError + System.lineSeparator() +
                "}" + System.lineSeparator();
    }
}
