package org.filipandrei.pandemic.model.entities;

import jdk.jshell.spi.ExecutionControl;
import kotlin.NotImplementedError;
import org.filipandrei.pandemic.model.math.Vector2;

public class Workplace extends Building {
    public Workplace(int id, int simId, Vector2 position, Vector2 size) {
        super(id, simId, position, size, Type.WORKPLACE);
    }


    @Override
    public boolean isAllowedToEnter(Person p) {
        throw new NotImplementedError(); // TODO
    }

    /**
     * @param sim
     */
    @Override
    public void update(ReadOnlySimulation sim) {

    }
}
