package org.filipandrei.pandemic.model.entities;

public abstract class Vehicle extends Entity implements ReadOnlyVehicle{

    /**
     * Constructs a new entity with the given ID and simulation ID.
     *
     * @param id    the unique ID of the entity
     * @param simId the ID of the simulation this entity belongs to
     */
    protected Vehicle(int id, int simId) {
        super(id, simId);
    }
}
