package org.filipandrei.pandemic.model.entities;

import kotlin.NotImplementedError;

public class Old extends Person {
    public Old(Person other) {
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
