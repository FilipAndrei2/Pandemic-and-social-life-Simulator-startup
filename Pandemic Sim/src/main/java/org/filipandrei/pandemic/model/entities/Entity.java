package org.filipandrei.pandemic.model.entities;

/**
 * Base class for the entity system.
 * Represents a generic entity in the system (person, adult, family, building, etc.).
 */
public abstract class Entity {

    /**
     * Unique ID of the entity, used by the system and persisted in the database.
     * Only positive values are allowed.
     */
    @Persisted public final int id;

    /**
     * ID of the simulation this entity belongs to.
     */
    @Persisted public final short simId;

    /**
     * Quick getter for the associated simulation.
     *
     * @return the Simulation object corresponding to this entity's simId
     */
    public static Simulation getSimulation() {
        // TODO: IMPLEMENT A FLYWEIGHT SimulationRegister
        // return SimulationRegister.get(simId);
        return null;
    }

    /**
     * Constructs a new entity with the given ID and simulation ID.
     *
     * @param id the unique ID of the entity
     * @param simId the ID of the simulation this entity belongs to
     */
    protected Entity(final int id, final short simId) {
        this.id = id;
        this.simId = simId;
    }
}
