package org.filipandrei.pandemic.model.entities;

import org.filipandrei.pandemic.model.math.Vector2;

public  class Child extends Person {
    public Child(Person other) {
        super(other);
    }

    public Child(int id, short simId, int familyId, int maxHp, String name, boolean isInsideBuilding, Vehicle vehicle, House houseId, Vector2 position, InfectionState infectionState, LifeStage lifeStage, Profession profession, ActivityState activity, Mood mood) {
        super(id, simId, familyId, maxHp, name, isInsideBuilding, vehicle, houseId, position, infectionState, lifeStage, profession, activity, mood);
    }

    @Override
    public int getSpeed() {
        return 0;
    }
}
