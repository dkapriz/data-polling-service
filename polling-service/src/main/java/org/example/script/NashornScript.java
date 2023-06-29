package org.example.script;

import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.CommandException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class NashornScript implements Script {
    private static final String EX_MSG_SCRIPT_TYPE_ERROR = "Script result type mismatch";
    private static final String EX_MSG_SCRIPT_EXEC_ERROR = "Script execute exception";
    private static final String EX_MSG_SCRIPT_READ_FILE_ERROR = "Error reading the script file";

    @Override
    public String run(String file) throws CommandException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        try {
            Object result = engine.eval(readFile(file));
            if (result instanceof String) {
                return result.toString();
            } else {
                log.error(EX_MSG_SCRIPT_TYPE_ERROR);
                throw new CommandException(EX_MSG_SCRIPT_TYPE_ERROR);
            }
        } catch (final ScriptException ex) {
            log.debug(ex.getMessage());
            log.error(EX_MSG_SCRIPT_EXEC_ERROR);
            throw new CommandException(EX_MSG_SCRIPT_EXEC_ERROR);
        } catch (IOException ex) {
            log.debug(ex.getMessage());
            log.error(EX_MSG_SCRIPT_READ_FILE_ERROR);
            throw new CommandException(EX_MSG_SCRIPT_READ_FILE_ERROR);
        }
    }

    private String readFile(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
            return stringBuilder.toString();
        }
    }
}
