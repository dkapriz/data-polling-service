package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class App {

    public static void main(String[] args) {

        log.info("Launching the service");

        TimerTask task = new PollingTask();
        Timer timer = new Timer("Timer");
        timer.schedule(task, 0, 10);
    }
}
