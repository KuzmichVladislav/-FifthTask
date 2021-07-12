package com.company.task5.entity;


import com.company.task5.util.TerminalIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Terminal {

    private static final Logger logger = LogManager.getLogger();
    private final long terminalId;
    private LogisticsCenter logisticsCenter;
   // private int palletNumber;

    public Terminal() {
        terminalId = TerminalIdGenerator.generateId();
    }

    public long getTerminalId() {
        return terminalId;
    }

   // public int getPalletNumber() {
   //     return palletNumber;
  //  }

    public void process(Truck truck) {
        truck.setTruckStatus(Truck.Status.PROCESSING);
        logisticsCenter = LogisticsCenter.getInstance();
        logger.info("At terminal " + getTerminalId() + " unloading of the truck" + truck.getTruckId() + " has begun");

        try {
            TimeUnit.MILLISECONDS.sleep(100 * truck.getTruckCapacity());
        } catch (InterruptedException e) {
            logger.error("Caught an exception: ", e);
            Thread.currentThread().interrupt();
        }
        if (truck.getAction() == Truck.Action.UPLOADING){
            uploadToCenter(truck);
        }else {
            unloadFromCenter(truck);
        }
        truck.setTruckStatus(Truck.Status.FINISHED);
    }

    private void uploadToCenter(Truck truck) {
        logisticsCenter.processTruck(truck.getTruckCapacity());
        logger.info("At terminal " + getTerminalId() + " unloading of the truck" + truck.getTruckId() + " is completed");
    }

    private void unloadFromCenter(Truck truck) {
        logisticsCenter.processTruck(-(truck.getTruckCapacity()));
        logger.info("At terminal " + getTerminalId() + " uploading of the truck" + truck.getTruckId() + " is completed");
    }
}
