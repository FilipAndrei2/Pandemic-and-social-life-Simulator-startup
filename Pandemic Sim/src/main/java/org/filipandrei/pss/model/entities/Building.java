package org.filipandrei.pss.model.entities;

import org.filipandrei.pss.model.math.Vector2;

public abstract class Building extends Entity {
    public Vector2 pos;
    public Vector2 dimensions;

    public abstract String type();
}