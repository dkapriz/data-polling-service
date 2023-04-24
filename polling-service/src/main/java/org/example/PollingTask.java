package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.command.ProcessLauncher;
import org.example.command.ProcessLauncherFactory;
import org.example.exceptions.CommandException;
import org.example.exceptions.NoSuchOperationSystemException;
import org.example.iniconfig.IniConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimerTask;

@Slf4j
public class PollingTask extends TimerTask {
    public static final String FILE_PATH = "output.txt";
    private long delay = 1000L;

    @SneakyThrows
    public void run() {
        IniConfig iniConfig = new IniConfig();
        iniConfig.readFile();
        if (iniConfig.getConfig() != null) {

           delay = iniConfig.getConfig().getServerSettings().getInterval();
            StateResponseDto stateResponseDto = executeCommand(
                    iniConfig.getConfig().getMetricSettings().getPingLocalhost(),
                    iniConfig.getConfig().getServerSettings().getConsoleEncoding());

            try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
                try (BufferedWriter bufferWriter = new BufferedWriter(writer)) {
                    bufferWriter.write(stateResponseDto.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Error writing to file");
            }
        }
        Thread.sleep(delay);
    }

    public StateResponseDto executeCommand(String command, String encoding) {
        ProcessLauncherFactory processLauncherFactory = new ProcessLauncherFactory();
        ProcessLauncher processLauncher;
        try {
            processLauncher = processLauncherFactory.getLauncher();
        } catch (NoSuchOperationSystemException e) {
            log.error("Command execution error - OS is not supported");
            return getStateResponseDto(command, "Request failed", "OS is not supported");
        }
        try {
            return getStateResponseDto(command, processLauncher.sendCommand(command, encoding));
        } catch (CommandException e) {
            return getStateResponseDto(command, "Request failed", e.getMessage());
        }
    }

    private StateResponseDto getStateResponseDto(String metricName, String metricValue, String metricError) {
        return StateResponseDto.builder()
                .initDefaultField()
                .metricName(metricName)
                .metricValue(metricValue)
                .executionError(metricError)
                .build();
    }

    private StateResponseDto getStateResponseDto(String metricName, String metricValue) {
        return getStateResponseDto(metricName, metricValue, "NaN");
    }
}
