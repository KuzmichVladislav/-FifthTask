package com.company.task5.entity;

import com.company.task5.util.VanIdGenerator;

public class Truck extends Thread {
    private final long vanId;
    private boolean perishable;
    private int truckCapacity;
    private Task task;
    private State vanState;

    public Truck(boolean perishable, int truckCapacity) {
        this.vanId = VanIdGenerator.generateId();
        this.perishable = perishable;
        this.truckCapacity = truckCapacity;
        task = Task.LOAD;
        this.vanState = State.NEW;
    }

    public long getVanId() {
        return vanId;
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

    public State getVanState() {
        return vanState;
    }

    public void setVanState(State vanState) {
        this.vanState = vanState;
    }

    @Override
    public void run() {
        LogisticsCenter base = LogisticsCenter.getInstance();
        Terminal terminal = base.acquireTerminal(perishable);
        terminal.process(this);

        switch (task) {
            case LOAD -> base.addPallet();
            case UNLOAD -> base.removePallet();
        }
        base.releaseTerminal(terminal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Truck)) return false;

        Truck truck = (Truck) o;

        if (getVanId() != truck.getVanId()) return false;
        if (isPerishable() != truck.isPerishable()) return false;
        if (getTask() != truck.getTask()) return false;
        return getVanState() == truck.getVanState();
    }

    @Override
    public int hashCode() {
        int result = (int) (getVanId() ^ (getVanId() >>> 32));
        result = 31 * result + (isPerishable() ? 1 : 0);
        result = 31 * result + (getTask() != null ? getTask().hashCode() : 0);
        result = 31 * result + (getVanState() != null ? getVanState().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Van{");
        sb.append("vanId=").append(vanId);
        sb.append(", perishable=").append(perishable);
        sb.append(", task=").append(task);
        sb.append(", vanState=").append(vanState);
        sb.append('}');
        return sb.toString();
    }

    public enum State {
        NEW, PROCESSING, FINISHED;
    }

    public enum Task {
        LOAD, UNLOAD;
    }
}
