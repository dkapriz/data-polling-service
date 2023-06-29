package org.example.command;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.CommandException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CommandExecute {
    public static final String EX_MSG_PROCESS_CREATION_ERROR = "Command failed - Process creation error";
    public static final String EX_MSG_INTERRUPT_ERROR = "Command failed - The process is interrupted";
    public static final String EX_MSG_TIMEOUT = "Command failed - Timeout exceeded";
    public static final String EX_MSG_UNKNOWN_ERROR = "Command failed - Unknown error";

    private CommandExecute() {
    }

    public static final int TIMEOUT = 10;

    /**
     * Метод производит запуск команды в командной строке операционной системы. После запуска команды в отдельном
     * потоке происходит ожидание ее выполнения и в случае успешного выполнения возвращается строковый результат.
     *
     * @param command  - команда запроса адаптированная под тип операционной системы
     * @param encoding - кодировка, в которой операционная системы производит вывод результата выполнения команды
     * @return String - результат выполнения команды
     * @throws CommandException - Ошибка выполнения команды (ошибка создания процесса, таймаут ожидания выполнения,
     *                          команда прервана, ошибки другого характера)
     */
    public static String run(List<String> command, String encoding) throws CommandException {
        String result = "Request failed";
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        builder.directory(new File(System.getProperty("user.home")));
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            log.error(EX_MSG_PROCESS_CREATION_ERROR);
            log.debug(e.getMessage());
            throw new CommandException(EX_MSG_PROCESS_CREATION_ERROR);
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        WaitStream waitStream = new WaitStream(process.getInputStream(), encoding);
        Future<String> future = executorService.submit(waitStream);
        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            log.debug(e.getMessage());
            log.error(EX_MSG_INTERRUPT_ERROR);
            throw new CommandException(EX_MSG_INTERRUPT_ERROR);
        }
        assert exitCode == 0;
        try {
            result = future.get(TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            log.debug(ex.getMessage());
            log.error(EX_MSG_TIMEOUT);
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            log.error(EX_MSG_TIMEOUT);
            ex.printStackTrace();
            future.cancel(true);
            throw new CommandException(EX_MSG_TIMEOUT);
        }
        executorService.shutdown();
        if (result.isEmpty()) {
            throw new CommandException(EX_MSG_UNKNOWN_ERROR);
        }
        return result;
    }
}


