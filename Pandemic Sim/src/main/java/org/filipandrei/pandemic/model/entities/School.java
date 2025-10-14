package org.filipandrei.pandemic.model.entities;

import kotlin.NotImplementedError;
import org.filipandrei.pandemic.model.math.Vector2;

public class School extends Building {
    public School(int id, short simId, Vector2 position, Vector2 size) {
        super(id, simId, position, size, Type.SCHOOL);
    }

    @Override
    public boolean isAllowedToEnter(Person p) {
        throw new NotImplementedError(); // TODO
    }
}
