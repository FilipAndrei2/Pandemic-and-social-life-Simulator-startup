package org.filipandrei.pss.model.entities;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class World {
    public World(int simulationId, String simulationName) {
        this.simulationId = simulationId;
        this.simulationName = simulationName;
    }

    public int simulationId;
    public String simulationName;
    /**
     * key -> id
     * value -> entity ref
     */
    public Map<Integer, Entity> entities = new ConcurrentHashMap<>();

    /**
     * key -> id
     * value -> lock of group
     */
    public Map<Integer, ReadWriteLock> locks = new ConcurrentHashMap<>();

    public boolean registerEntity(@NotNull Entity e) {
        boolean succ = null == entities.put(e.id, e);
        if (!locks.containsKey(e.id/50)) {
            if (null != locks.put(e.id/50, new ReentrantReadWriteLock())){
                throw new RuntimeException("Failed to create lock for entity group " + e.id/50);
            }
        }
        return succ;
    }

    public boolean eraseEntity(int id) {
        return null != entities.remove(id);
    }
    
    public void clear() {
        entities.clear();
        locks.clear();
    }
}
