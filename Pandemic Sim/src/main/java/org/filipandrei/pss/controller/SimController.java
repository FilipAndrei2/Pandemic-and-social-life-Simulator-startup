package org.filipandrei.pss.controller;

import org.filipandrei.pss.model.Model;
import org.filipandrei.pss.view.View;
import org.jetbrains.annotations.NotNull;

public class SimController extends Controller {

    private boolean isRunning = true;
    public SimController(@NotNull Model model, @NotNull View view) {
        super(model, view);
    }

    @Override
    public void start() {
        model.startSimulation();
        view.createView();

        isRunning = false;
        runMainLoop();
    }

    @Override
    public void runMainLoop() {
        while (isRunning) {
            model.tick();
            view.draw();
        }
    }

    @Override
    public void stop() {
        isRunning = false;
        model.stopSimulation();
        view.destroyView();
    }

}
