package ru.sfu.querang.utils;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class TestLogger {

    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void log(Object o) {
        out.println("[log] " + o);
    }

    public static void log(Object o, String who) {
        out.println(String.format("[log] --- <%s> -> ", who) + o);
    }
}
