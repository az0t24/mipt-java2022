package ru.khairullin.ar;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Ship implements Runnable {
    private final Logger logger;
    private final int capacity;
    private final int shipCode;
    private static final long TUNNEL_SPEED = 1000L;
    private final Dock dock;
    private final ArrayBlockingQueue<Ship> tunnel;
    private final AtomicBoolean success = new AtomicBoolean(false);

    public Ship(int capacity, Dock dock, ArrayBlockingQueue<Ship> tunnel, int shipNumber, Logger logger) {
        this.capacity = capacity;
        this.dock = dock;
        this.tunnel = tunnel;
        this.shipCode = shipNumber;
        this.logger = logger;
        success.set(false);
    }

    /**
     * Run a ship task
     */
    @Override
    public void run() {
        logger.log(Level.INFO, "Судно " + shipCode + ": создано");
        try {
            tunnel.put(this);
            logger.log(Level.INFO, "Судно " + shipCode + ": вошло в туннель");
            Thread.sleep(TUNNEL_SPEED);
            logger.log(Level.INFO, "Судно " + shipCode + ": вышло из туннеля");
            tunnel.take();

            while (!success.get()) {
                if (dock.getIsFree().compareAndSet(true, false)) {
                    dock.load(this);

                    success.set(true);
                }
            }
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Судно " + shipCode + ": ошибка!");
            Thread.currentThread().interrupt();
        }
    }

    public int getShipCode() {
        return shipCode;
    }

    public int getCapacity() {
        return capacity;
    }
}
