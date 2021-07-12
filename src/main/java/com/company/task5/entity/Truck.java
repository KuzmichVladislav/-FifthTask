package com.company.task5.entity;


import com.company.task5.util.TruckIdGenerator;

public class Truck extends Thread {
    private final long truckId;
    private boolean perishable;
    private int truckCapacity;
    private Task task;
    private Status truckStatus;

    public Truck(boolean perishable, int truckCapacity) {
        this.truckId = TruckIdGenerator.generateId();
        this.perishable = perishable;
        this.truckCapacity = truckCapacity;
        task = Task.LOAD;
        this.truckStatus = Status.NEW;
    }

    public long getTruckId() {
        return truckId;
    }

    public boolean isPerishable() {
        return perishable;
    }

    public void setPerishable(boolean perishable) {
        this.perishable = perishable;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public void setTruckCapacity(int truckCapacity) {
        this.truckCapacity = truckCapacity;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Status getTruckStatus() {
        return truckStatus;
    }

    public void setTruckStatus(Status truckStatus) {
        this.truckStatus = truckStatus;
    }

    @Override
    public void run() {
        LogisticsCenter base = LogisticsCenter.getInstance();
        Terminal terminal = base.acquireTerminal(perishable);
        terminal.process(this);

        if (task == Task.LOAD) {
            base.addPallet();
        } else if (task == Task.UNLOAD) {
            base.removePallet();
        }
        base.releaseTerminal(terminal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Truck)) return false;

        Truck truck = (Truck) o;

        if (getTruckId() != truck.getTruckId()) return false;
        if (isPerishable() != truck.isPerishable()) return false;
        if (getTask() != truck.getTask()) return false;
        return getTruckStatus() == truck.getTruckStatus();
    }

    @Override
    public int hashCode() {
        int result = (int) (getTruckId() ^ (getTruckId() >>> 32));
        result = 31 * result + (isPerishable() ? 1 : 0);
        result = 31 * result + (getTask() != null ? getTask().hashCode() : 0);
        result = 31 * result + (getTruckStatus() != null ? getTruckStatus().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Truck{");
        sb.append("truckId=").append(truckId);
        sb.append(", perishable=").append(perishable);
        sb.append(", truckCapacity=").append(truckCapacity);
        sb.append(", task=").append(task);
        sb.append(", truckStatus=").append(truckStatus);
        sb.append('}');
        return sb.toString();
    }

    public enum Status {
        NEW, PROCESSING, FINISHED;
    }

    public enum Task {
        LOAD, UNLOAD;
    }
}
