package org.example.task;

import lombok.extern.slf4j.Slf4j;
import org.example.command.ProcessLauncher;
import org.example.command.ProcessLauncherFactory;
import org.example.exceptions.CommandException;
import org.example.exceptions.NoSuchOperationSystemException;
import org.example.script.NashornScript;
import org.example.script.Script;
import org.example.task.dto.StateResponseDto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class Task {
    private static final String FILE_OUTPUT_PATH = "output.txt";
    private static final String REQUEST_FAILED = "Request failed";

    protected void saveResultToFile(String result, String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            try (BufferedWriter bufferWriter = new BufferedWriter(writer)) {
                bufferWriter.write(result);
            }
        } catch (IOException ex) {
            log.debug(ex.getMessage());
            log.error("Error writing to file");
        }
    }

    protected void saveResultToFile(String result) {
        saveResultToFile(result, FILE_OUTPUT_PATH);
    }

    protected StateResponseDto executeConsoleCommand(String metricName, String command, String regex, String encoding) {
        ProcessLauncherFactory processLauncherFactory = new ProcessLauncherFactory();
        ProcessLauncher processLauncher;
        try {
            processLauncher = processLauncherFactory.getLauncher();
        } catch (NoSuchOperationSystemException ex) {
            log.debug(ex.getMessage());
            log.error("Command execution error - OS is not supported");
            return getStateResponseDto(metricName, command, REQUEST_FAILED, "OS is not supported");
        }
        try {
            String commandResult = processLauncher.sendCommand(command, encoding);
            return checkString(commandResult, regex) ?
                    getStateResponseDto(metricName, command, commandResult) :
                    getStateResponseDto(metricName, command, commandResult, "Wrong command result");
        } catch (CommandException ex) {
            log.debug(ex.getMessage());
            return getStateResponseDto(metricName, command, REQUEST_FAILED, ex.getMessage());
        }
    }

    protected StateResponseDto executeScriptCommand(String metricName, String fileName, String regex) {
        Script script = new NashornScript();
        try {
            String scriptResult = script.run(fileName);
            return checkString(scriptResult, regex) ?
                    getStateResponseDto(metricName, fileName, scriptResult) :
                    getStateResponseDto(metricName, fileName, scriptResult,
                            "Wrong script result");
        } catch (CommandException ex) {
            log.debug(ex.getMessage());
            return getStateResponseDto(metricName, fileName,
                    REQUEST_FAILED, ex.getMessage());
        }
    }

    private boolean checkString(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean result = matcher.find();
        if (!result) {
            log.info("the result of executing the command does not match the pattern");
        }
        return result;
    }

    private StateResponseDto getStateResponseDto(String metricName, String execCommand,
                                                 String metricValue, String metricError) {
        return StateResponseDto.builder()
                .initDefaultField()
                .metricName(metricName)
                .execCommand(execCommand)
                .metricValue(metricValue)
                .executionError(metricError)
                .build();
    }

    private StateResponseDto getStateResponseDto(String metricName, String execCommand, String metricValue) {
        return getStateResponseDto(metricName, execCommand, metricValue, "NaN");
    }
}
