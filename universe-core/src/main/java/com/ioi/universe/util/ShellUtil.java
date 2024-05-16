package com.ioi.universe.util;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ShellUtil {

    private final static long DEFAULT_TIME_OUT = 10000L;

    private ShellUtil() {
    }


    public static Process exec(String command) {
        return exec(command, DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
    }

    public static Process exec(String command, long timeout, TimeUnit unit) {
        return exec(null, null, command);
    }

    public static Process exec(String workPath, Map<String, String> envs, String command) {
        if (command != null && !command.isBlank()) {
            List<String> commands = Arrays.stream(command.split(" ")).collect(Collectors.toList());
            return exec(workPath, envs, commands);
        }
        return null;
    }

    public static Process exec(List<String> command) {
        return exec(null, null, command);
    }


    public static Process exec(String workPath, Map<String, String> envs, List<String> command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);
        if (workPath != null) {
            processBuilder.directory(new File(workPath));
        }
        if (envs != null) {
            processBuilder.environment().putAll(envs);
        }
        processBuilder.command(command);
        try {
            Process process = processBuilder.start();
            return process;
        } catch (IOException e) {
            throw new RuntimeException("command[{" + command + "}] start faild.", e);
        }
    }


    public static void destroy(Process process) {
        if (process != null) {
            process.destroyForcibly();
        }
    }
}
