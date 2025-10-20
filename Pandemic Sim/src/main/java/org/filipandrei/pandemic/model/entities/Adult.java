package org.filipandrei.pandemic.model.entities;

import kotlin.NotImplementedError;
import org.filipandrei.pandemic.model.math.Vector2;

public class Adult extends Person {
    Adult(int id, short simId, int familyId, int maxHp, String name, boolean isInsideBuilding, Vehicle vehicle, House house, Vector2 position, InfectionState infectionState, Profession profession, ActivityState activity, Mood mood) {
        super(id, simId, house.getFamilyId(), maxHp, name, isInsideBuilding, vehicle, house, position, infectionState, LifeStage.ADULT, profession, activity, mood);
    }

    public Adult(Person other) {
        super(other);
    }

    @Override
    public int getSpeed() {
        return 0; // TODO: IMPLEMENT
    }


    @Override
    public void update(ReadOnlySimulation sim) {
        throw new NotImplementedError(); // TODO
    }
}
