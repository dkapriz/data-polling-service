package org.example.command;

import org.example.exceptions.NoSuchOperationSystemException;

public class ProcessLauncherFactory {
    public ProcessLauncher getLauncher() throws NoSuchOperationSystemException {
        return switch (getOS()) {
            case LINUX -> new LinuxLauncher();
            case WINDOWS -> new WindowsLauncher();
        };
    }

    private OperationSystem getOS() throws NoSuchOperationSystemException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")){
            return OperationSystem.LINUX;
        }
        if (os.contains("win")){
            return OperationSystem.WINDOWS;
        }
        throw new NoSuchOperationSystemException("The operating system type is not set");
    }
}
