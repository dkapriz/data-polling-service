package org.example.task;

import lombok.extern.slf4j.Slf4j;
import org.example.iniconfig.IniConfig;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskFactory {
    private static final long MAX_TIMEOUT = 5000L;
    private static final String EX_MSG_TIMEOUT_ERROR = "ExecutorService closing time exceeded";
    private static final int POOL_SIZE = 3;
    private IniConfig iniConfig;
    private ScheduledExecutorService executor;

    public TaskFactory() {
        executor = new ScheduledThreadPoolExecutor(POOL_SIZE);
        log.info("START SCHEDULER");
    }

    public void setIniConfig(IniConfig iniConfig) {
        this.iniConfig = iniConfig;
    }

    public void run(TaskType taskType) {
        switch (taskType) {
            case CHECK_CPU -> executor.scheduleAtFixedRate(new CheckCPUTask(iniConfig),0L,
                    iniConfig.getConfig().getCheckCPU().getInterval(), TimeUnit.MILLISECONDS);
            case CHECK_URL -> executor.scheduleAtFixedRate(new CheckURLTask(iniConfig),0L,
                    iniConfig.getConfig().getCheckURL().getInterval(), TimeUnit.MILLISECONDS);
            case PING_HOST -> executor.scheduleAtFixedRate(new PingHostTask(iniConfig),0L,
                    iniConfig.getConfig().getPingHost().getInterval(), TimeUnit.MILLISECONDS);
        }
    }

    public void restart(){
        stop();
        executor = new ScheduledThreadPoolExecutor(POOL_SIZE);
        log.info("START SCHEDULER");
    }

    public void stop() {
        if (executor != null) {
            executor.shutdown();
            long start = System.currentTimeMillis();
            while (!executor.isTerminated()) {
                if (System.currentTimeMillis() > start + MAX_TIMEOUT) {
                    log.error(EX_MSG_TIMEOUT_ERROR);
                    return;
                }
            }
            log.info("TERMINATE SCHEDULER");
        }
    }
}
