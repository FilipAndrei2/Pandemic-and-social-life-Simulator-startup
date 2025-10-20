package org.filipandrei.pandemic.model.entities;

import kotlin.NotImplementedError;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Family extends Entity {

    /**
     * Numarul maximi de membri din familie.
     */
    public static final int MAX_NUMBER_OF_MEMBERS = 20;
    @Persisted protected List<Integer> memberIds = new CopyOnWriteArrayList<>();

    private final String familyName;

    public Family(int id, int simId, String familyName, Simulation sim) {
        super(id, simId);
        this.familyName = familyName;
    }

    public void addFamilyMember(Person p) throws RuntimeException {
        throw new NotImplementedError(); // TODO
    }


    public String getFamilyName() {
        return familyName;
    }

    public boolean deleteFamilyMember(int memberId) {
        return memberIds.remove(Integer.valueOf(memberId));
    }

    public boolean isInFamily(int memberId) {
        return memberIds.contains(memberId);
    }

    public List<Integer> getMemberIds() {
        return Collections.unmodifiableList(memberIds);
    }

    @Override
    public void update(ReadOnlySimulation sim) {

    }
}
