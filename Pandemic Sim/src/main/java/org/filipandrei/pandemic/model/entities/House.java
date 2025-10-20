package org.filipandrei.pandemic.model.entities;

import kotlin.NotImplementedError;
import org.filipandrei.pandemic.model.math.Vector2;

public class House extends Building {
    public House(int id, int simId, int familyId, Vector2 position, Vector2 size) {
        super(id, simId, position, size, Type.HOUSE);
        this.familyId = familyId;
    }

    protected final int familyId;

    public int getFamilyId() {
        return this.familyId;
    }

    @Override
    public boolean isAllowedToEnter(Person p) {
        throw new NotImplementedError();
    }

    @Override
    public void update(ReadOnlySimulation sim) {

    }
}
