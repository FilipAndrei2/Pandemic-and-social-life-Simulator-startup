package org.filipandrei.pandemic.model.entities;

import org.filipandrei.pandemic.model.math.Vector2;

public abstract class Building extends Entity implements ReadOnlyBuilding {

    @Persisted
    private final Vector2 position;
    @Persisted
    private final Vector2 size;
    @Persisted
    private final Type type;

    public Building(int id, int simId, Vector2 position, Vector2 size, Type type) {
        super(id, simId);
        this.position = position;
        this.size = size;
        this.type = type;
    }

    @Override
    public Vector2 getPositionDeepCopy() {
        return new Vector2(position.x, position.y);
    }

    @Override
    public Vector2 getSizeDeepCopy() {
        return new Vector2(size.x, size.y);
    }

    @Override
    public Type getType() {
        return this.type;
    }

    public abstract boolean isAllowedToEnter(Person p);

    public enum Type {
        HOUSE,
        SCHOOL,
        WORKPLACE
    }
}
