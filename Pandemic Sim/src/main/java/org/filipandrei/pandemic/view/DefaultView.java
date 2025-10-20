package org.filipandrei.pandemic.view;

import org.filipandrei.pandemic.model.configs.Configs;
import org.filipandrei.pandemic.model.entities.ReadOnlySimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class DefaultView implements View {

    private static final String WINDOW_TITLE = Configs.get("window.title");
    private static final int WINDOW_WIDTH = Integer.parseInt(Configs.get("window.width"));
    private static final int WINDOW_HEIGHT = Integer.parseInt(Configs.get("window.height"));

    private int requestedSimulationId = -1;

    private JFrame frame;
    private JPanel panel;

    private Scenes scene;

    // Main menu buttons
    private JButton bNewSim;
    private JButton bLoadSim;

    // Callbacks / listeners
    private Callable<Collection<String>> simulationsNamesProvider;
    private Callable<ReadOnlySimulation> simulationDataProvider;
    private Runnable onSimulationStart;
    private Runnable onSimulationPause;
    private Runnable onSimulationStop;
    private Consumer<Integer> onSimulationLoadById;

    public DefaultView() {
        frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);
        frame.setContentPane(panel);

        frame.pack();
        frame.setVisible(true);

        scene = Scenes.MAIN_MENU_SCREEN;
        displayMainMenu();
    }

    public enum Scenes {
        MAIN_MENU_SCREEN,
        SIMULATION_SCREEN
    }

    /* -------------------- VIEW SCENES -------------------- */

    @Override
    public void displayMainMenu() {
        if (scene != Scenes.MAIN_MENU_SCREEN) {
            panel.removeAll();
        }

        // Create buttons
        bNewSim = new JButton("New Simulation");
        bLoadSim = new JButton("Load Simulation");

        // Example positioning (manual)
        bNewSim.setBounds(50, 50, 150, 30);
        bLoadSim.setBounds(50, 100, 150, 30);

        // Attach dummy action listeners (to be replaced by callbacks)
        bNewSim.addActionListener(e -> {
            if (onSimulationStart != null) onSimulationStart.run();
        });

        bLoadSim.addActionListener(e -> {
            if (onSimulationLoadById != null) onSimulationLoadById.accept(requestedSimulationId);
        });

        panel.add(bNewSim);
        panel.add(bLoadSim);

        panel.revalidate();
        panel.repaint();
        scene = Scenes.MAIN_MENU_SCREEN;
    }

    @Override
    public void displaySimulation() {
        panel.removeAll();
        // TODO: Add simulation-specific components here
        panel.revalidate();
        panel.repaint();
        scene = Scenes.SIMULATION_SCREEN;
    }

    /**
     *
     */
    @Override
    public void showPauseMenu() {

    }

    /**
     *
     */
    @Override
    public void hidePauseMenu() {

    }

    /* -------------------- INPUT / KEY LISTENER -------------------- */

    @Override
    public void setKeyListener(KeyListener kl) {
        frame.addKeyListener(kl);
    }

    /* -------------------- CALLBACK SETTERS -------------------- */

    @Override
    public void setOnSimulationsNamesRequested(Callable<Collection<String>> provider) {
        this.simulationsNamesProvider = provider;
    }

    @Override
    public void setOnSimulationDataRequested(Callable<ReadOnlySimulation> provider) {
        this.simulationDataProvider = provider;
    }

    @Override
    public void setOnSimulationStart(Runnable listener) {
        this.onSimulationStart = listener;
    }

    @Override
    public void setOnSimulationPause(Runnable listener) {
        this.onSimulationPause = listener;
    }

    @Override
    public void setOnSimulationStop(Runnable listener) {
        this.onSimulationStop = listener;
    }

    @Override
    public void setOnSimulationLoadByIdRequested(Consumer<Integer> callback) {
        this.onSimulationLoadById = callback;
    }

    /**
     * @param cbProgramTerminationHandler
     */
    @Override
    public void setOnProgramTerminationRequest(Runnable cbProgramTerminationHandler) {

    }

    /**
     * @param listener
     */
    @Override
    public void setOnSimulationResume(Runnable listener) {

    }

    /**
     * @param listener
     */
    @Override
    public void setOnSimulationSaveRequested(Runnable listener) {

    }

    /**
     * @param cbDeleteSimulation
     */
    @Override
    public void setOnSimulationDeleteByIdRequest(Consumer<Integer> cbDeleteSimulation) {

    }
}
