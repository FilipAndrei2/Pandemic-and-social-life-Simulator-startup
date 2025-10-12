package org.filipandrei.pss.view;

import org.filipandrei.pss.model.SimulationModel;
import org.jetbrains.annotations.NotNull;

public abstract class SimulationView {

    protected SimulationModel model;

    protected SimulationView(@NotNull SimulationModel model) {
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null");
        }
        this.model = model;
    }
    public abstract void createView();
    public abstract void destroyView();


    public abstract void draw();
    public abstract void drawGrid();
    public abstract void drawWorld();
}
