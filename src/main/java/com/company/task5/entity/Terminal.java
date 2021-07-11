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
        truck.setVanState(Truck.State.PROCESSING);
        logger.info("Terminal " + terminalId + " started processing van " + truck.getVanId());

        try {
            TimeUnit.MILLISECONDS.sleep(truck.getTruckCapacity() * 100);
        } catch (InterruptedException e) {
            logger.error("Caught an exception: ", e);
            Thread.currentThread().interrupt();
        }
        truck.setVanState(Truck.State.FINISHED);
        palletNumber = truck.getTruckCapacity();
        logger.info("Terminal " + terminalId + " finished processing van " + truck.getVanId());
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
