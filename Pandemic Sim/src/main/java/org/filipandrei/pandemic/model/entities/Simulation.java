package org.filipandrei.pandemic.model.entities;

import kotlin.NotImplementedError;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Encapsulates the context of a simulation.
 * <p>
 * Each entity in the simulation references this simulation via its {@code simId} field.
 * </p>
 */
public class Simulation {

    /**
     * Unique ID of the simulation.
     * <p>
     * Found in every entity of the simulation via the {@code simId} field.
     * </p>
     */
    public final int simId;

    /**
     * Name of the simulation.
     */
    private String name;

    /** Map of entities in the simulation, keyed by their unique ID. */
    private Map<Integer, Entity> entities = new ConcurrentHashMap<>();

    private Queue<Integer> freedIds = new LinkedList<>();
    private static int nextId = 0;

    public Simulation(int simId) {
        this.simId = simId;
    }

    /**
     * Returns an entity by its unique ID.
     *
     * @param id the ID of the entity to retrieve
     * @return the entity corresponding to the given ID, or {@code null} if no entity is mapped to that ID
     */
    public Entity getEntityById(int id) {
        return entities.get(Integer.valueOf(id));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNextId() {
        Integer id = freedIds.poll();
        if (id != null) {
            return id;
        }
        if (nextId == Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("Trying to create new entites. Maximum number of entities aquired!");
        }
        return nextId++;
    }

    /**
     * Deletes an entity, freeing its ID and scheduling its removal from the database.
     * <p>
     * If no entity with the given ID is registered, the operation has no effect.
     * In any case, the provided ID is added to the list of IDs available for reuse.
     * </p>
     *
     * @param id the unique ID of the entity to delete
     */
    public void deleteEntity(int id) {
        if (!freedIds.contains(id)) {
            freedIds.add(id);
        }
        if (null == getEntityById(id)) {
            return;
        }
        entities.remove(id);
    }

    /**
     * Attempts to add an entity to the simulation.
     * <p>
     * Note: Ensure that the {@code Entity} object is in a valid state before adding.
     * </p>
     * <p>
     * If an entity with the same ID is already registered, an {@link UnsupportedOperationException} is thrown.
     * Similarly, if the entity is already mapped to another key, an exception is thrown.
     * </p>
     *
     * @param e the entity to attempt to add
     * @return {@code false} if the entity was already added, {@code true} if the entity was added successfully
     * @throws UnsupportedOperationException if another entity already has the same ID, or if this entity is already mapped
     */
    public boolean addEntity(Entity e) {
        Entity existing = entities.get(e.id);
        if (existing == e) {
            return false; // already added
        }
        if (existing != null) {
            throw new UnsupportedOperationException("ID already mapped to another entity");
        }
        if (entities.containsValue(e)) {
            throw new UnsupportedOperationException("Entity already mapped to a different ID");
        }
        entities.put(e.id, e);
        return true;
    }

    public boolean replaceEntity(Entity e) {
        throw new NotImplementedError(); // TODO
    }
}
