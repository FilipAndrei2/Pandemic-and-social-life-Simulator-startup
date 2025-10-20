package org.filipandrei.pandemic.model.entities;

import org.filipandrei.pandemic.model.math.Vector2;

public interface ReadOnlyPerson extends ReadOnlyEntity {
    int getFamilyId();
    int getMaxHp();
    int getHp();
    boolean isInsideBuilding();
    String getName();
    Vehicle getVehicle();
    int getHouseId();
    Vector2 getPosition();
    Person.InfectionState getInfectionState();
    Person.LifeStage getLifeStage();
    Person.Profession getProfession();
    Person.ActivityState getActivity();
    Person.Mood getMood();
}
