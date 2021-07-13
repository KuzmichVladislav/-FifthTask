package com.company.task5.util;

public class TerminalIdGenerator {

    private static long count;

    private TerminalIdGenerator() {
    }

    public static long generateId() {
        return ++count;
    }
}
