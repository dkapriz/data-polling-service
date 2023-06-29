package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.iniconfig.IniConfig;
import org.example.task.TaskFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.concurrent.TimeUnit;

import static org.example.task.TaskType.CHECK_CPU;
import static org.example.task.TaskType.CHECK_URL;
import static org.example.task.TaskType.PING_HOST;


@Slf4j
public class App {
    private static final long DEFAULT_DELAY = 1000L;
    private static final int MAX_COUNT_CONFIG_ERROR = 100;
    private static final Marker CONSOLE_MARKER = MarkerFactory.getMarker("CONSOLE");

    public static void main(String[] args) {
        log.info(CONSOLE_MARKER, "Service started");
        long delay = DEFAULT_DELAY;
        int configError = 0;
        TaskFactory taskFactory = new TaskFactory();

        while (configError <= MAX_COUNT_CONFIG_ERROR) {
            IniConfig iniConfig = new IniConfig();
            iniConfig.readFile();
            if (iniConfig.getConfig() != null) {
                delay = iniConfig.getConfig().getServerSettings().getInterval().longValue();
                configError = 0;

                taskFactory.restart();
                taskFactory.setIniConfig(iniConfig);
                taskFactory.run(PING_HOST);
                taskFactory.run(CHECK_CPU);
                taskFactory.run(CHECK_URL);

            } else {
                configError++;
            }
            delay(delay);
        }
    }

    private static void delay(long value) {
        try {
            TimeUnit.MILLISECONDS.sleep(value);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}