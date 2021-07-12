package com.company.task5.entity;


import com.company.task5.util.TerminalIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Terminal {

    private static final Logger logger = LogManager.getLogger();
    private final long terminalId;
    private int palletNumber;

    public Terminal() {
        terminalId = TerminalIdGenerator.generateId();
    }

    public long getTerminalId() {
        return terminalId;
    }

    public int getPalletNumber() {
        return palletNumber;
    }

    public void process(Truck truck) {
        truck.setTruckStatus(Truck.Status.PROCESSING);
        logger.info("At terminal " + getTerminalId() + " unloading of the truck" + truck.getTruckId() + " has begun");

        try {
            TimeUnit.MILLISECONDS.sleep(100 * truck.getTruckCapacity());
        } catch (InterruptedException e) {
            logger.error("Caught an exception: ", e);
            Thread.currentThread().interrupt();
        }
        truck.setTruckStatus(Truck.Status.FINISHED);
        palletNumber = truck.getTruckCapacity();
        logger.info("At terminal " + getTerminalId() + " unloading of the truck" + truck.getTruckId() + " is completed");
    }

    @Override
    public int hashCode() {
        return Long.hashCode(terminalId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Terminal terminal = (Terminal) obj;
        return terminalId == terminal.terminalId;
    }

    @Override
    public String toString() {
        return "Terminal " + terminalId;
    }
}
