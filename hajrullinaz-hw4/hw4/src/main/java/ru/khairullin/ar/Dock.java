package ru.khairullin.ar;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Dock {
    private final int dockType;
    private static final long SPEED = 100;
    private final Logger logger;
    private final AtomicBoolean isFree = new AtomicBoolean(true);

    public Dock(int dockType, Logger logger) {
        this.dockType = dockType;
        this.logger = logger;
    }

    /**
     * Load a ship
     * @param ship ship to load
     */

    public void load(Ship ship) throws InterruptedException {
        isFree.set(false);

        logger.log(Level.INFO, "Причал " + dockType + ": началась погрузка судна " + ship.getShipCode());
        Thread.sleep(ship.getCapacity() * SPEED);
        logger.log(Level.INFO, "Причал " + dockType + ": завершилась погрузка судна " + ship.getShipCode());

        isFree.set(true);
    }

    public AtomicBoolean getIsFree() {
        return isFree;
    }
}
