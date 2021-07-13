package com.company.task5.entity;

import com.company.task5.util.TerminalIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Terminal {

    private static final Logger logger = LogManager.getLogger();
    private final long terminalId;
    private LogisticsCenter logisticsCenter;

    public Terminal() {
        terminalId = TerminalIdGenerator.generateId();
    }

    public long getTerminalId() {
        return terminalId;
    }

    public void process(Truck truck) {
        truck.setTruckStatus(Truck.Status.PROCESSING);
        logisticsCenter = LogisticsCenter.getInstance();

        try {
            TimeUnit.MILLISECONDS.sleep(100 * truck.getTruckCapacity());
        } catch (InterruptedException e) {
            logger.error("Caught an exception: ", e);
            Thread.currentThread().interrupt();
        }
        if (truck.getAction() == Truck.Action.LOADING) {
            loadToCenter(truck);
        } else {
            shipmentFromCenter(truck);
        }
        truck.setTruckStatus(Truck.Status.FINISHED);
    }

    private void loadToCenter(Truck truck) {
        logisticsCenter.processTruck(truck.getTruckCapacity());
        logger.info("At terminal " + getTerminalId() + ", " + truck.getTruckCapacity() + " pallets were loaded from truck " + truck.getTruckId());
    }

    private void shipmentFromCenter(Truck truck) {
        logisticsCenter.processTruck(-(truck.getTruckCapacity()));
        logger.info("At terminal " + getTerminalId() + ", " + truck.getTruckCapacity() + " pallets were shipped onto truck " + truck.getTruckId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Terminal terminal = (Terminal) o;

        if (terminalId != terminal.terminalId) {
            return false;
        }
        return logisticsCenter != null ? logisticsCenter.equals(terminal.logisticsCenter) : terminal.logisticsCenter == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (terminalId ^ (terminalId >>> 32));
        result = 31 * result + (logisticsCenter != null ? logisticsCenter.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Terminal{");
        sb.append("terminalId=").append(terminalId);
        sb.append(", logisticsCenter=").append(logisticsCenter);
        sb.append('}');
        return sb.toString();
    }
}
