package org.filipandrei.pandemic.model.entities;

import kotlin.NotImplementedError;
import org.filipandrei.pandemic.model.math.Vector2;

public class Infant extends Person {

    public Infant(int id, short simId, int familyId, int maxHp, String name, boolean isInsideBuilding,  House houseId, Vector2 position, InfectionState infectionState, ActivityState activity, Mood mood) {
        super(id, simId, familyId, maxHp, name, isInsideBuilding, null, houseId, position, infectionState, LifeStage.INFANT, Profession.UNEMPLOYED, activity, mood);
    }

    public Infant(Person other) {
        super(other);
    }

    @Override
    public int getSpeed() {
        throw new NotImplementedError(); // TODO
    }

    /**
     * @param sim
     */
    @Override
    public void update(ReadOnlySimulation sim) {

    }
}
