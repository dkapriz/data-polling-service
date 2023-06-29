package org.example.script;

import org.example.exceptions.CommandException;

public interface Script {
    String run(String file) throws CommandException;
}
