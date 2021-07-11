package com.company.task5.parser;

import com.company.task5.entity.Truck;


public class DataParser {
    private static final String WHITE_SPACE_DELIMITER_REGEX = "\\s+";

    public Truck parseVan(String dataString) {
        String[] params = dataString.split(WHITE_SPACE_DELIMITER_REGEX);
        boolean perishable = Boolean.parseBoolean(params[0]);
        int truckCapacity = Integer.parseInt(params[1]);
        return new Truck(perishable, truckCapacity);
    }
}
