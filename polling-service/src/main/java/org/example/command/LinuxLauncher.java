package org.example.command;

import org.example.exceptions.CommandException;

import java.util.ArrayList;
import java.util.List;

public class LinuxLauncher implements ProcessLauncher {
    @Override
    public String sendCommand(String command, String encoding) throws CommandException {
        List<String> commandPartList = new ArrayList<>();
        commandPartList.add("/bin/bash");
        commandPartList.add("-c");
        commandPartList.add(command);
        return CommandExecute.run(commandPartList, encoding);
    }
}