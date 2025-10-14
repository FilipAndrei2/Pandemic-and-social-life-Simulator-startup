package org.filipandrei.pandemic.model.entities;

import org.filipandrei.pandemic.model.math.Vector2;

public abstract class Person extends Entity {



    /**
     * Intervale de valori pentru maxHp
     * Infant: 60-100
     * Child: 150-250
     * Adult: 300-500
     * Old:   120-350
     */
    public static final int INFANT_MAX_HP_LOWER_LIMIT = 60;
    public static final int INFANT_MAX_HP_UPPER_LIMIT = 100;
    public static final int CHILD_MAX_HP_LOWER_LIMIT = 150;
    public static final int CHILD_MAX_HP_UPPER_LIMIT = 250;
    public static final int ADULT_MAX_HP_LOWER_LIMIT = 300;
    public static final int ADULT_MAX_HP_UPPER_LIMIT = 500;
    public static final int OLD_MAX_HP_LOWER_LIMIT = 120;
    public static final int OLD_MAX_HP_UPPER_LIMIT = 350;

    @Persisted
    public int familyId;
    @Persisted
    protected int maxHp;
    @Persisted
    protected int hp = maxHp;
    @Persisted
    protected final String name;
    @Persisted
    protected boolean isInsideBuilding = false;
    @Persisted
    protected Vehicle vehicle = null;
    @Persisted
    protected House houseId;
    @Persisted
    protected Vector2 position;
    @Persisted
    protected InfectionState infectionState;
    @Persisted
    protected LifeStage lifeStage;
    @Persisted
    protected Profession profession;
    @Persisted
    protected ActivityState activity;
    @Persisted
    protected Mood mood;

    public Person(int id, short simId, int familyId, int maxHp, String name, boolean isInsideBuilding, Vehicle vehicle, House houseId, Vector2 position, InfectionState infectionState, LifeStage lifeStage, Profession profession, ActivityState activity, Mood mood) {
        super(id, simId);
        this.familyId = familyId;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.name = name;
        this.isInsideBuilding = isInsideBuilding;
        this.vehicle = vehicle;
        this.houseId = houseId;
        this.position = position;
        this.infectionState = infectionState;
        this.lifeStage = lifeStage;
        this.profession = profession;
        this.activity = activity;
        this.mood = mood;
    }

    public Person(Person other) {
        this(
                other.id,
                other.simId,
                other.familyId,
                other.maxHp,
                other.name,
                other.isInsideBuilding,
                other.vehicle,
                other.houseId,
                other.position,
                other.infectionState,
                other.lifeStage,
                other.profession,
                other.activity,
                other.mood
        );
    }

    public int getFamilyId() {
        return this.familyId;
    }

    public int getMaxHp() {
        return this.maxHp;
    }

    public int getHp() {
        return this.hp;
    }

    public String getName() {
        return this.name;
    }

    public boolean isInsideBuilding() {
        return this.isInsideBuilding;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public House getHouseId() {
        return this.houseId;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public InfectionState getInfectionState() {
        return this.infectionState;
    }

    public LifeStage getLifeStage() {
        return this.lifeStage;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public ActivityState getActivity() {
        return this.activity;
    }

    public Mood getMood() {
        return this.mood;
    }

    /**
     * Defines the movement speed of a {@code Person} or its subclass.
     * <p>
     * Overload this method in subclasses of {@code Person} to define the computation rule for movement speed.
     * A speed of 0 means the object cannot move.
     * A speed of 100 represents the normal reference speed (100%).
     * A speed of 200 represents double the reference speed.
     * Technically, the value could go up to {@link Integer#MAX_VALUE},
     * but 200 was chosen to provide a balanced stat.
     * </p>
     *
     * @return an integer between 0 and 200 representing the movement speed of this object
     */
    public abstract int getSpeed();

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setVehicle(Vehicle vehicle) {
        if (this.vehicle != null) { // Nu poti sa treci din vehicul direct in altul
            this.vehicle = vehicle;
        }
    }

    public void setX(double x) {
        this.position.x = x;
    }

    public void setY(double y) {
        this.position.y = y;
    }

    public void setPostion(double x, double y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setInfectionState(InfectionState infectionState) {
        this.infectionState = infectionState;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public void setActivity(ActivityState activity) {
        this.activity = activity;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    /**
     * Advances the person to the next life stage.
     * <p>
     * If this person is already at the {@link LifeStage#OLD} stage,
     * this method has no effect and the {@code lifeStage} field remains unchanged.
     * </p>
     * @return a new {@code Person} instance representing the same individual in the next life stage,
     *         or {@code this} if the person is already at the {@link LifeStage#OLD} stage
     */
    public Person growOlder() {
        if (this.lifeStage.state == LifeStage.OLD.state) {
            return this; // already old
        }
        this.lifeStage = LifeStage.valueOf(this.lifeStage.state + 1);
        return PersonFactory.createPerson(this.lifeStage, this);
    }

    /**
     * Attempts to infect this person.
     * <p>
     * The infection succeeds only if the person is neither {@link InfectionState#DEAD}
     * nor {@link InfectionState#IMMUNE}. If successful, the {@code infectionState}
     * is set to {@link InfectionState#INFECTED}.
     * </p>
     *
     * @return {@code true} if the person was successfully infected, {@code false} otherwise
     */
    public final boolean infect() {
        if (this.infectionState != InfectionState.DEAD && this.infectionState != InfectionState.IMMUNE) {
            this.infectionState = InfectionState.INFECTED;
            return true;
        }
        return false;
    }

    public enum InfectionState {
        DEAD, HEALTHY, INFECTED, IMMUNIZED, VACCINATED, IMMUNE
    }

    public enum LifeStage {
        INFANT(0), CHILD(1), ADULT(2), OLD(3);
        private int state;

        LifeStage(int state) {
            this.state = state;
        }

        public int getState() {
            return this.state;
        }

        public static LifeStage valueOf(int state) {
            for (LifeStage enumValue : LifeStage.values()) {
                if (enumValue.state == state) {
                    return enumValue;
                }
            }
            throw new IllegalArgumentException("Invalid state: " + state);
        }
    }

    public enum Profession {
        UNEMPLOYED,
        STUDENT,
        TEACHER,
        WORKER
    }

    public enum ActivityState {
        IDLE,
        WALKING,
        WORKING,
        SLEEPING,
        SPEAKING
    }

    public enum Mood {
        NEUTRAL,
        HAPPY,
        SAD,
        ANGRY
    }

}
