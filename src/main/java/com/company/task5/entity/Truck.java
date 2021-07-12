package com.company.task5.entity;


import com.company.task5.util.TruckIdGenerator;

public class Truck implements Runnable {
    private final long truckId;
    private boolean perishable;
    private int truckCapacity;
    private Status truckStatus;

    public Truck(boolean perishable, int truckCapacity) {
        this.truckId = TruckIdGenerator.generateId();
        this.perishable = perishable;
        this.truckCapacity = truckCapacity;
        this.truckStatus = Status.NEW;
    }

    public long getTruckId() {
        return truckId;
    }

    public boolean isPerishable() {
        return perishable;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public void setTruckStatus(Status truckStatus) {
        this.truckStatus = truckStatus;
    }

    @Override
    public void run() {
        LogisticsCenter base = LogisticsCenter.getInstance();
        Terminal terminal = base.acquireTerminal(isPerishable());
        terminal.process(this);
        base.addPallet();
        base.releaseTerminal(terminal);
    }

    public enum Status {
        NEW, PROCESSING, FINISHED;
    }
}
