package org.filipandrei.pss.model.entities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Family extends Entity {
    public List<Integer> membersIds = Collections.synchronizedList(new LinkedList<>());
    public String lastName;
    public int homeId;
}
