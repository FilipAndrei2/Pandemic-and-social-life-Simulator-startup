package org.filipandrei.pss.view;

import org.filipandrei.pss.model.Model;
import org.jetbrains.annotations.NotNull;

public abstract class View {

    protected Model model;

    protected View(@NotNull Model model) {
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null");
        }
        this.model = model;
    }
    public abstract void createView();
    public abstract void destroyView();
    public abstract void drawGrid();

    public abstract void draw();
}
