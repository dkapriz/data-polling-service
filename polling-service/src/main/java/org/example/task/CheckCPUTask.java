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
public class CheckCPUTask extends Task implements Runnable {
    private IniConfig config;

    @Override
    public void run() {
        saveResultToFile(executeConsoleCommand(config.getConfig().getCheckCPU().getMetricName(),
                config.getConfig().getCheckCPU().getExecCommand(),
                config.getConfig().getCheckCPU().getMetricPattern(),
                config.getConfig().getServerSettings().getConsoleEncoding()).toString());
        log.info("Command is execute - " + config.getConfig().getCheckCPU().getMetricName() + " - " +
                config.getConfig().getCheckCPU().getExecCommand());
    }
}
