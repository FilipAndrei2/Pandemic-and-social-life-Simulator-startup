package org.filipandrei.pandemic.model.entities;

import kotlin.NotImplementedError;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Encapsulates the context of a simulation.
 * <p>
 * Each entity in the simulation references this simulation via its {@code simId} field.
 * </p>
 */
public class Simulation extends Entity implements ReadOnlySimulation {

    private int entityCount = 0;
    /**
     * Name of the simulation.
     */
    private String name;

    /** Map of entities in the simulation, keyed by their unique ID. */
    private Map<Integer, Entity> entities = new ConcurrentHashMap<>();

    private Queue<Integer> freedIds = new LinkedList<>();
    private int nextId = 0;

    /**
     * Creates a simulation with {@code entityCount} set to 0
     * @param id
     * @param name
     */
    public Simulation(int id, String name) {
        super(id, id);
        this.name = name;
    }

    public Simulation(int id, String name,int entityCount) {
        super(id, id);
        this.name = name;
        this.entityCount = entityCount;
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

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getEntityCount() {
        return this.entityCount;
    }

    @Override
    public Map<Integer, ReadOnlyEntity> getEntitiesReadOnly() {
        return entities.entrySet().stream().collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));
    }

    private int getNextId() {
        Integer id = freedIds.poll();
        if (id != null) {
            return id;
        }
        if (nextId == Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("Trying to create new entities. Maximum number of entities acquired!");
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
        if (entityCount < 1) {
            return;
        }
        if (null != entities.remove(id)){
            --entityCount;
        }
        if (!freedIds.contains(id)) {
            freedIds.add(id);
        }
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
     * @return id of added entity on success, {@code 0} if entity was already registered, {@code -1} if entity was not added because entity count limit was reached
     * @throws UnsupportedOperationException if another entity already has the same ID, or if this entity is already mapped
     */
    public int addEntity(Entity e) {
        if (entityCount == Integer.MAX_VALUE) {
            return -1;
        }
        Entity existing = entities.get(e.getId());
        if (existing == e) {
            return 0; // already added
        }
        if (existing != null) {
            throw new UnsupportedOperationException("ID already mapped to another entity");
        }
        if (entities.containsValue(e)) {
            throw new UnsupportedOperationException("Entity already mapped to a different ID");
        }
        entities.put(e.getId(), e);
        ++entityCount;
        return e.getId();
    }

    public void update() {
        this.entities.values()
                .forEach( e -> e.update(this));
    }

    @Override
    public void update(ReadOnlySimulation sim) {
    }
}
