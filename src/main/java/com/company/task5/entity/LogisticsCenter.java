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
    private final Deque<Terminal> availableTerminals = new ArrayDeque<>();
    private final Deque<Condition> waitingThreads = new ArrayDeque<>();
    private final int capacity;
    private final int maxWorkload;
    private final double lowerLoadThreshold;
    private final double upperLoadThreshold;
    private final AtomicInteger currentWorkload = new AtomicInteger(0);
    private int palletNumber;

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

    public static LogisticsCenter getInstance() {
        return LoadSingleton.INSTANCE;
    }

    public Terminal acquireTerminal(boolean perishable) {
        try {
            acquireReleaseLock.lock();
            if (availableTerminals.isEmpty()) {
                try {
                    Condition condition = acquireReleaseLock.newCondition();
                    if (perishable) {
                        waitingThreads.addFirst(condition);
                    } else {
                        waitingThreads.addLast(condition);
                    }
                    condition.await();
                } catch (InterruptedException e) {
                    logger.error(e.getStackTrace());
                    Thread.currentThread().interrupt();
                }
            }
            return availableTerminals.removeFirst();
        } finally {
            acquireReleaseLock.unlock();
        }
    }

    public void releaseTerminal(Terminal terminal) {
        try {
            acquireReleaseLock.lock();

            if (availableTerminals.size() <= capacity) {
                availableTerminals.push(terminal);
                Condition condition = waitingThreads.pollFirst();
                if (condition != null) {
                    condition.signal();
                }
            }
        } finally {
            acquireReleaseLock.unlock();
        }
        palletNumber = terminal.getPalletNumber();
    }

    public void addPallet() {
        currentWorkload.addAndGet(getPalletNumber());
    }

    public void removePallet() {
        currentWorkload.addAndGet(-1000);
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
                    removePallet();
                    logger.info("The warehouse is " + loadFactor + " percent full, removed 1000 pallet");
                    workload = currentWorkload.get();
                    loadFactor = (double) workload / maxWorkload;
                }
            }
        }, 0, 1000);
    }

    public int getPalletNumber() {
        return palletNumber;
    }

    private static class LoadSingleton {
        static final LogisticsCenter INSTANCE = new LogisticsCenter(10, 3000, 0.1, 0.95);
    }
}
