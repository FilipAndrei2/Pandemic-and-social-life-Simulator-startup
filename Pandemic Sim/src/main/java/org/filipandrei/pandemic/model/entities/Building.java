package org.filipandrei.pandemic.model.entities;

import org.filipandrei.pandemic.model.math.Vector2;

public abstract class Building extends Entity {

    @Persisted
    public final Vector2 position;
    @Persisted
    public final Vector2 size;
    @Persisted
    public final Type type;

    public Building(int id, short simId, Vector2 position, Vector2 size, Type type) {
        super(id, simId);
        this.position = position;
        this.size = size;
        this.type = type;
    }

    public abstract boolean isAllowedToEnter(Person p);

    public enum Type {
        HOUSE,
        SCHOOL,
        WORKPLACE
    }
}
