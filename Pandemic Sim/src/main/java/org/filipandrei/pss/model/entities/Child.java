package org.filipandrei.pss.model.entities;

import org.filipandrei.pss.model.math.Vector2;

public abstract class Child extends Person {
    public int schoolId;
    public Child(double x, double y, byte maxHp, Vector2 pos, InfectionState infectionState, int familyId, String firstName, int schoolId) {
        super(x, y, maxHp, pos, infectionState, familyId, firstName);
        this.schoolId = schoolId;
    }
}
