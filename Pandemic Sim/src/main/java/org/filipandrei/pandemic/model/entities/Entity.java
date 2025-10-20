package org.filipandrei.pandemic.model.entities;


import lombok.Data;

/**
 * Base class for the entity system.
 * Represents a generic entity in the system (person, adult, family, building, etc.).
 */
@Data
public abstract class Entity implements ReadOnlyEntity {

    /**
     * Unique ID of the entity, used by the system and persisted in the database.
     * Only positive values are allowed.
     */
    @Persisted 
    final int id;

    /**
     * ID of the simulation this entity belongs to.
     */
    @Persisted private final int simId;

    /**
     * Constructs a new entity with the given ID and simulation ID.
     *
     * @param id the unique ID of the entity
     * @param simId the ID of the simulation this entity belongs to
     */
    protected Entity(final int id, final int simId) {
        this.id = id;
        this.simId = simId;
    }

    public abstract void update(ReadOnlySimulation sim);

}
