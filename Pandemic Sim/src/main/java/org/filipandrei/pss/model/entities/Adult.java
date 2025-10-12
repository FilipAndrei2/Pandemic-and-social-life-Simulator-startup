package org.filipandrei.pss.model.entities;

import org.filipandrei.pss.model.math.Vector2;


public abstract class Adult extends Person {
    public int workBuildingId;

    /**
     * -1 for unmarried
     */
    public int spouseId;

    public Adult(double x, double y, byte maxHp, Vector2 pos, InfectionState infectionState, int familyId, String firstName) {
        super(x, y, maxHp, pos, infectionState, familyId, firstName);
    }
}
