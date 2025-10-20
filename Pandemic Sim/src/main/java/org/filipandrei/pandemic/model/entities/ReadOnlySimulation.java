package org.filipandrei.pandemic.model.entities;

import java.util.Map;

public interface ReadOnlySimulation extends ReadOnlyEntity {
    String getName();
    int getEntityCount();

    /**
     * Method for obtaining a snapshot of registered entities.
     * <p>
     *  Use {@code instanceof} with coresponding entity view classes to check the type of each ReadOnlyEntity.
     * </p>
     * @return an unmodifiable {@link Map} of entity views mapped to their respective id.
     * @see ReadOnlyEntity
     * @see ReadOnlyPerson
     * @see ReadOnlyBuilding
     * @see ReadOnlyVehicle
     */
    Map<Integer, ReadOnlyEntity> getEntitiesReadOnly();
}
