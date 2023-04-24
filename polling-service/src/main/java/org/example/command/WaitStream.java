package org.example.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class WaitStream implements Callable<String> {
    private final InputStream inputStream;
    private final String encoding;

    public WaitStream(InputStream inputStream, String encoding) {
        this.inputStream = inputStream;
        this.encoding = encoding;
    }

    public WaitStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.encoding = "UTF-8";
    }

    @Override
    public String call() throws UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(inputStream, encoding))
                .lines().collect(Collectors.joining(" "));
    }
}
