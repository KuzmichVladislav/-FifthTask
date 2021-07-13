package com.company.task5.entity;

import com.company.task5.util.TruckIdGenerator;

public class Truck implements Runnable {

    private final long truckId;
    private Action action;
    private boolean perishable;
    private int truckCapacity;
    private Status truckStatus;

    public enum Status {
        NEW, PROCESSING, FINISHED
    }

    public enum Action {
        LOADING, SHIPMENT;
    }

    public Truck(boolean perishable, int truckCapacity, Action action) {
        this.action = action;
        this.truckId = TruckIdGenerator.generateId();
        this.perishable = perishable;
        this.truckCapacity = truckCapacity;
        this.truckStatus = Status.NEW;
    }

    public Action getAction() {
        return action;
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
        LogisticsCenter logisticCenter = LogisticsCenter.getInstance();
        Terminal terminal = null;
        terminal = logisticCenter.acquireTerminal(isPerishable());
        terminal.process(this);
        logisticCenter.releaseTerminal(terminal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Truck) {
            Truck truck = (Truck) o;
            if (getTruckId() != truck.getTruckId()) {
                return false;
            }
            if (isPerishable() != truck.isPerishable()) {
                return false;
            }
            if (getTruckCapacity() != truck.getTruckCapacity()) {
                return false;
            }
            return truckStatus == truck.truckStatus;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = (int) (getTruckId() ^ (getTruckId() >>> 32));
        result = 31 * result + (isPerishable() ? 1 : 0);
        result = 31 * result + getTruckCapacity();
        result = 31 * result + (truckStatus != null ? truckStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Truck{");
        sb.append("truckId=").append(truckId);
        sb.append(", perishable=").append(perishable);
        sb.append(", truckCapacity=").append(truckCapacity);
        sb.append(", truckStatus=").append(truckStatus);
        sb.append('}');
        return sb.toString();
    }
}
