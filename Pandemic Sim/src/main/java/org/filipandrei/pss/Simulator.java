package org.filipandrei.pss;

import org.filipandrei.pss.controller.Controller;
import org.filipandrei.pss.controller.SimController;
import org.filipandrei.pss.model.Model;
import org.filipandrei.pss.model.SimModel;
import org.filipandrei.pss.view.SwingSimViewImpl;
import org.filipandrei.pss.view.View;

public class Simulator {
    public static void main(String[] args) {
        Model model = new SimModel();
        View view = new SwingSimViewImpl(model);
        Controller controller = new SimController(model, view);
        controller.start();
    }
}
