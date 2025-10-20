package org.filipandrei.pandemic.main;

import org.filipandrei.pandemic.controller.Controller;
import org.filipandrei.pandemic.model.Model;
import org.filipandrei.pandemic.view.View;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultController implements Controller, KeyListener {

    private final Model model;
    private final View view;
    private final AtomicBoolean isSimulationRunning = new AtomicBoolean(false);
    private final AtomicBoolean isProgramRunning = new AtomicBoolean(true);
    private ScreenState screenState = ScreenState.MAIN_MENU_SCREEN;

    public DefaultController(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Porneste controllerul: seteaza callback-urile View si initiaza bucla principala.
     */
    @Override
    public void start() {
        setViewCallbacks();
        mainLoop();
    }

    private void setViewCallbacks() {
        view.setKeyListener(this);
        view.setOnSimulationDataRequested(model::getReadOnlySimulation);
        view.setOnSimulationLoadByIdRequested(id -> {
            if (!model.readActiveSimulation(id)) {
                throw new RuntimeException("Can't load simulation! Requested id: " + id);
            }
        });
        view.setOnSimulationsNamesRequested(model::getSimulationsNames);
        view.setOnSimulationResume(() -> isSimulationRunning.set(true));
        view.setOnSimulationPause(() -> isSimulationRunning.set(false));
        view.setOnSimulationStop(this::sendSignalToModelToEndAndSaveCurrentSimulation);
        view.setOnProgramTerminationRequest(this::endProgram);
        view.setOnSimulationStart(() -> {
            changeScreen(ScreenState.SIMULATION_SCREEN);
            isSimulationRunning.set(true);
        });
        view.setOnSimulationSaveRequested(model::updateActiveSimulation);
        view.setOnSimulationDeleteByIdRequest(model::deleteSimulation);
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> {
                changeScreen(ScreenState.PAUSE_MENU_SCREEN);
            }
            default -> { }
        }
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void sendSignalToModelToEndAndSaveCurrentSimulation() {
        model.endAndSaveActiveSim();
    }

    private void endProgram() {
        model.endAndSaveActiveSim();
        isSimulationRunning.set(true); // might be optional
        isProgramRunning.set(false); // programul o sa iasa din bucla de control curat si frumos
    }

    private int targetTps = 100;
    private void mainLoop() {
        while (isProgramRunning.get()) {
            if (screenState == ScreenState.SIMULATION_SCREEN && isSimulationRunning.get()) {
                model.tick();
            }
        }
    }

    private void changeScreen(ScreenState newState) {
        if (screenState == newState) {
            return;
        }

        if (screenState == ScreenState.PAUSE_MENU_SCREEN) {
            view.hidePauseMenu();
            screenState = ScreenState.SIMULATION_SCREEN;
            return;
        }

        switch (newState) {
            case MAIN_MENU_SCREEN -> view.displayMainMenu();
            case SIMULATION_SCREEN -> view.displaySimulation();
            case PAUSE_MENU_SCREEN -> view.showPauseMenu();
        }
        screenState = newState;
    }

    private enum ScreenState {
        MAIN_MENU_SCREEN,
        SIMULATION_SCREEN,
        PAUSE_MENU_SCREEN;
    }
}
