package org.filipandrei.pss.controller;

import org.filipandrei.pss.model.Model;
import org.filipandrei.pss.view.View;
import org.jetbrains.annotations.NotNull;

public abstract class Controller {
    protected Model model = null;
    protected View view = null;

    public Controller(@NotNull Model model, @NotNull View view) {
        if (model == null) {
            throw new IllegalArgumentException("model cannot be null");
        }
        if (view == null) {
            throw new IllegalArgumentException("view cannot be null");
        }
        this.model = model;
        this.view = view;
    }

    public abstract void start();
    public abstract void runMainLoop();
    public abstract void stop();
}
