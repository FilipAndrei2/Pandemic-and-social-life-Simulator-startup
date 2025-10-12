package org.filipandrei.pss.model.entities;

import org.filipandrei.pss.model.math.Vector2;

public abstract class Person extends Entity {
    /**
     * Positive value
     */
    public final byte maxHp;
    public byte hp;
    public Vector2 pos;
    public InfectionState infectionState;
    public int familyId;
    public String firstName;

    public Person(double x, double y, byte maxHp, Vector2 pos, InfectionState infectionState, int familyId, String firstName) {
        this.infectionState = infectionState;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.pos = pos;
        this.familyId = familyId;
        this.firstName = firstName;
    }

    public static enum InfectionState {
        DEAD, HEALTHY, INFECTED, IMMUNIZED, IMMUNE;

        @Override
        public String toString() {
            return switch (this) {
                case DEAD       -> "DEAD";
                case HEALTHY    -> "HEALTHY";
                case INFECTED   -> "INFECTED";
                case IMMUNIZED  -> "IMMUNIZED";
                case IMMUNE     -> "IMMUNE";
            };
        }
    }

    public abstract String ageToString();
}
