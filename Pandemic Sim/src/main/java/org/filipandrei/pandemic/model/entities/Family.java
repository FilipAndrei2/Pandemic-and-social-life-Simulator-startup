package org.filipandrei.pandemic.model.entities;

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

    public Family(int id, short simId, String familyName) {
        super(id, simId);
        this.familyName = familyName;
    }

    /**
     * Incearca sa adauge un nou membru in familie
     * @param memberId id-ul membrului care incearca sa-l adauge
     * @throws RuntimeException Daca a fost atinsa limita numarului de membri {@code MAX_NUMBER_OF_MEMBERS}. Daca id-ul nu este asociat unui obiect de tip Person
     */
    public void addFamilyMember(int memberId) throws RuntimeException {
        if (memberIds.size() >= MAX_NUMBER_OF_MEMBERS) {
            throw new RuntimeException("Trying to add member to family " + this.id + ". Family is full!");
        }
        Entity e = this.getSimulation().getEntityById(memberId);
        if (e == null) {
            throw new RuntimeException("Trying to insert member " + memberId + " into family " + id + ". Member not found!");
        }
        if (!(e instanceof Person)) {
            throw new RuntimeException("Trying to insert member " + memberId + " into family " + id + ". Id of member references to entity of type " + e.getClass().getName() + "!");
        }
        Person p = (Person)e;
        if (p.getFamilyId() != this.id ) {
            p.setFamilyId(id);
        }
        if (!memberIds.contains(memberId)) {
            memberIds.add(memberId);
        }
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
}
