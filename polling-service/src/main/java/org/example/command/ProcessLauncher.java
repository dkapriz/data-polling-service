package org.example.command;

import org.example.exceptions.CommandException;

public interface ProcessLauncher {
    String sendCommand(String command, String encoding) throws CommandException;
}