package org.filipandrei.pandemic.main;

import org.filipandrei.pandemic.model.DefaultModel;
import org.filipandrei.pandemic.view.DefaultView;

public class Simulator {
    public static void main(String[] args) {
        new DefaultController(new DefaultModel(), new DefaultView()).start();
    }
}
