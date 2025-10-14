package org.filipandrei.pandemic.model.entities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Simulations {

    private static Map<Short, Simulation> simulations = new ConcurrentHashMap<>();

    /**
     * Return simulation from RAM.
     * <p>
     * </p>
     * @param simId
     * @return a reference to a {@code Simulation} object representing a simulation loaded from db, or {@code null}, if there is not any simulation mapped to {@code simId}
     */
    public static Simulation getSimulationById(short simId) {
        //
        return simulations.get(simId);
    }
}
