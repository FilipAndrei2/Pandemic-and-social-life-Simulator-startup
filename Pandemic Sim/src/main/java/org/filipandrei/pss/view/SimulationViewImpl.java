package org.filipandrei.pss.view;

import org.filipandrei.pss.model.SimulationModel;
import org.filipandrei.pss.model.entities.Entity;
import org.filipandrei.pss.model.entities.Person;
import org.filipandrei.pss.model.entities.World;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class SimulationViewImpl extends SimulationView {

    private JFrame frame = null;
    private JPanel panel = null;
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private static final String WINDOW_TITLE = "Pandemic Simulation";

    public SimulationViewImpl(@NotNull SimulationModel model) {
        super(model);
    }

    @Override
    public void createView() {
        this.frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setMaximumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setTitle(WINDOW_TITLE);
        frame.setResizable(false);

        this.panel = new JPanel(true);
        panel.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        panel.setMaximumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        frame.add(panel);
        frame.pack();
        panel.setVisible(true);
        frame.setVisible(true);
    }

    @Override
    public void destroyView() {
        panel = null;
        frame.dispose();
        frame = null;
    }

    @Override
    public void drawGrid() {

    }

    @Override
    public void drawWorld() {
        World worldRef = model.getWorld();
        Graphics g = panel.getGraphics();
        g.setColor(Color.GREEN);
        for (Entity e : worldRef.entities.values()) {
            if (!(e instanceof Person)) {
                continue;
            }
            Person p = (Person) e;
            g.drawOval((int) p.pos.x, (int) p.pos.y, 40, 40);
        }
    }


    @Override
    public void draw() {
        drawWorld();
    }
}
