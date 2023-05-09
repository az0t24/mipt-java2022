package ru.khairullin.ar;

import com.sun.tools.javac.Main;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import java.util.logging.LogManager;
import java.util.logging.Logger;


public class App {
    private static Logger logger;
    static {
        try (FileInputStream ins = new FileInputStream(
                "/home/az0t24/mipt-java/hajrullinaz-hw4/hw4/log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            logger = Logger.getLogger(Main.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    private static final int TUNNEL_SIZE = 5;
    private static final int RANDOM_NUMBER = 100;
    private static final int NUM_SHIPS = 20;
    private static final ArrayBlockingQueue<Ship> TUNNEL
            = new ArrayBlockingQueue<>(TUNNEL_SIZE);

    private static final ArrayList<Dock> DOCKS = new ArrayList<>(
            Arrays.asList(new Dock(0, logger),
                          new Dock(1, logger),
                          new Dock(2, logger)));
    private static final ArrayList<Integer> CAPACITIES =
            new ArrayList<>(Arrays.asList(10, 50, 100));

    public static void main(String[] args) {
        ArrayList<Ship> ships = new ArrayList<>();
        for (int i = 0; i < NUM_SHIPS; i++) {
            int dockType = (int) (Math.random() * RANDOM_NUMBER) % 3;
            int capacityType = (int) (Math.random() * RANDOM_NUMBER) % 3;
            ships.add(new Ship(CAPACITIES.get(capacityType), DOCKS.get(dockType), TUNNEL, i, logger));
        }

        for (Ship ship : ships) {
            new Thread(ship).start();
        }
    }
}
