package org.filipandrei.pandemic.model.entities;

import org.filipandrei.pandemic.model.db.SimulationDao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Simulations {

    private static Map<Integer, Simulation> simulations = new ConcurrentHashMap<>();

    /**
     * Retrieves a simulation by its ID.
     * <p>
     * If no simulation is found in the memory (RAM), a query is sent to the database.
     * If no simulation is mapped to the given {@code simId}, this method returns {@code null}.
     * </p>
     *
     * @param simId the ID of the simulation to retrieve
     * @return a reference to the {@code Simulation} object associated with the specified ID,
     *         or {@code null} if no such simulation exists
     */
    public static Simulation getSimulationById(int simId) {
        return simulations.computeIfAbsent(simId, id ->
                new SimulationDao().getById(id)
        );
    }
}
