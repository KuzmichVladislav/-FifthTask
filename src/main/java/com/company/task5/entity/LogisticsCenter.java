package com.company.task5.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogisticsCenter {

    private static final Logger logger = LogManager.getLogger();
    private final Lock acquireReleaseLock = new ReentrantLock();
    private final Deque<Terminal> terminals = new ArrayDeque<>();
    private final Deque<Terminal> availableTerminals = new ArrayDeque<>();
    private final Deque<Condition> waitingThreads = new ArrayDeque<>();
    private final int capacity;
    private final int maxWorkload;
    private final double lowerLoadThreshold;
    private final double upperLoadThreshold;
    private final AtomicInteger currentWorkload = new AtomicInteger(0);
    Condition condition = acquireReleaseLock.newCondition();

    private LogisticsCenter(int capacity, int maxWorkload, double lowerLoadThreshold, double upperLoadThreshold) {
        this.capacity = capacity;
        this.maxWorkload = maxWorkload;
        this.lowerLoadThreshold = lowerLoadThreshold;
        this.upperLoadThreshold = upperLoadThreshold;

        for (int i = 0; i < capacity; i++) {
            availableTerminals.add(new Terminal());
        }
        scheduleTrackTask();
    }

    private static class LoadSingleton {

        static final LogisticsCenter INSTANCE = new LogisticsCenter(10, 3000, 0.15, 0.85);
    }

    public static LogisticsCenter getInstance() {
        return LoadSingleton.INSTANCE;
    }

    public Terminal acquireTerminal(boolean perishable) {
        acquireReleaseLock.lock();
        try {
            while (availableTerminals.isEmpty()) {
                condition.await();
            }
            if (perishable) {
                waitingThreads.addFirst(condition);
            } else {
                waitingThreads.addLast(condition);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            acquireReleaseLock.unlock();
        }
        return availableTerminals.removeFirst();
    }

    public void releaseTerminal(Terminal terminal) {
        acquireReleaseLock.lock();
        condition = waitingThreads.pollFirst();
        try {
            availableTerminals.offer(terminal);
            condition.signal();
        } finally {
            acquireReleaseLock.unlock();
        }
    }

    public void processTruck(int palletNumber) {
        currentWorkload.addAndGet(palletNumber);
    }

    private void scheduleTrackTask() {
        Timer timer = new Timer(true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int workload = currentWorkload.get();
                double loadFactor = (double) workload / maxWorkload;
                while (loadFactor < lowerLoadThreshold) {
                    currentWorkload.addAndGet(100);
                    logger.info("the warehouse load is too small: " + loadFactor + " percent, 100 pallets were added");
                    workload = currentWorkload.get();
                    loadFactor = (double) workload / maxWorkload;
                }
                while (loadFactor > upperLoadThreshold) {
                    currentWorkload.addAndGet(-100);
                    logger.info("The warehouse is " + loadFactor + " percent full, removed 100 pallet");
                    workload = currentWorkload.get();
                    loadFactor = (double) workload / maxWorkload;
                }
            }
        }, 0, 1000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LogisticsCenter that = (LogisticsCenter) o;

        if (capacity != that.capacity) {
            return false;
        }
        if (maxWorkload != that.maxWorkload) {
            return false;
        }
        if (Double.compare(that.lowerLoadThreshold, lowerLoadThreshold) != 0) {
            return false;
        }
        if (Double.compare(that.upperLoadThreshold, upperLoadThreshold) != 0) {
            return false;
        }
        if (acquireReleaseLock != null ? !acquireReleaseLock.equals(that.acquireReleaseLock) : that.acquireReleaseLock != null) {
            return false;
        }
        if (terminals != null ? !terminals.equals(that.terminals) : that.terminals != null) {
            return false;
        }
        if (availableTerminals != null ? !availableTerminals.equals(that.availableTerminals) : that.availableTerminals != null) {
            return false;
        }
        if (waitingThreads != null ? !waitingThreads.equals(that.waitingThreads) : that.waitingThreads != null) {
            return false;
        }
        if (currentWorkload != null ? !currentWorkload.equals(that.currentWorkload) : that.currentWorkload != null) {
            return false;
        }
        return condition != null ? condition.equals(that.condition) : that.condition == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = acquireReleaseLock != null ? acquireReleaseLock.hashCode() : 0;
        result = 31 * result + (terminals != null ? terminals.hashCode() : 0);
        result = 31 * result + (availableTerminals != null ? availableTerminals.hashCode() : 0);
        result = 31 * result + (waitingThreads != null ? waitingThreads.hashCode() : 0);
        result = 31 * result + capacity;
        result = 31 * result + maxWorkload;
        temp = Double.doubleToLongBits(lowerLoadThreshold);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upperLoadThreshold);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (currentWorkload != null ? currentWorkload.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LogisticsCenter{");
        sb.append("acquireReleaseLock=").append(acquireReleaseLock);
        sb.append(", terminals=").append(terminals);
        sb.append(", availableTerminals=").append(availableTerminals);
        sb.append(", waitingThreads=").append(waitingThreads);
        sb.append(", capacity=").append(capacity);
        sb.append(", maxWorkload=").append(maxWorkload);
        sb.append(", lowerLoadThreshold=").append(lowerLoadThreshold);
        sb.append(", upperLoadThreshold=").append(upperLoadThreshold);
        sb.append(", currentWorkload=").append(currentWorkload);
        sb.append(", condition=").append(condition);
        sb.append('}');
        return sb.toString();
    }
}
