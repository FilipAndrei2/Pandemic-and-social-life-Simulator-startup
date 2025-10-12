package org.filipandrei.pss.model.entities;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Entity {
    private static int nextId = 0;
    private static ConcurrentLinkedQueue<Integer> freeIds = new ConcurrentLinkedQueue<>();
    public final int id;

    protected Entity() {
        if (freeIds.isEmpty()) {
            this.id = nextId++;
        } else {
            this.id = freeIds.remove();
        }
    }

    public void free() {
        freeIds.add(this.id);
    }
}
