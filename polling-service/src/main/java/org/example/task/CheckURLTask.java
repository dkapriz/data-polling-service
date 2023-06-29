package org.example.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.iniconfig.IniConfig;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class CheckURLTask extends Task implements Runnable {
    private IniConfig config;

    @Override
    public void run() {
        saveResultToFile(executeScriptCommand(config.getConfig().getCheckURL().getMetricName(),
                config.getConfig().getCheckURL().getExecCommand(),
                config.getConfig().getCheckURL().getMetricPattern()).toString());
        log.info("Command is execute - " + config.getConfig().getCheckURL().getMetricName() + " - " +
                config.getConfig().getCheckURL().getExecCommand());
    }
}
