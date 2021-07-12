package com.company.task5.parser;

import com.company.task5.entity.Truck;


public class DataParser {
    private static final String WHITE_SPACE_DELIMITER_REGEX = "\\s+";

    public Truck parseTruck(String dataString) {
        String[] truckParams = dataString.split(WHITE_SPACE_DELIMITER_REGEX);
        boolean perishable = Boolean.parseBoolean(truckParams[0]);
        int truckCapacity = Integer.parseInt(truckParams[1]);
        Truck.Action action = Truck.Action.valueOf(truckParams[2]);
        return new Truck(perishable, truckCapacity,action);
    }
}
