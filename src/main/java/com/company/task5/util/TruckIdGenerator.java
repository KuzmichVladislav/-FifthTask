package com.company.task5.util;

public class TruckIdGenerator {

    private static long count;

    private TruckIdGenerator() {
    }

    public static long generateId() {
        return ++count;
    }
}
