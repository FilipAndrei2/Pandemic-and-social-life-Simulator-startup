package org.filipandrei.pandemic.model.entities;

import org.filipandrei.pandemic.model.math.Vector2;

public interface ReadOnlyBuilding extends ReadOnlyEntity {
    Vector2 getPositionDeepCopy();
    Vector2 getSizeDeepCopy();
    Building.Type getType();
}
