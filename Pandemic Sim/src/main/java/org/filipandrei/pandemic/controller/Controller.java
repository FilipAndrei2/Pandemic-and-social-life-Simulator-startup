package org.filipandrei.pandemic.controller;

import org.filipandrei.pandemic.model.Model;
import org.filipandrei.pandemic.view.View;

/**
 * Clasa de legatura dintre interfetele Model si View
 */
public interface Controller {
    /**
     * Leaga modelul si viewul la controller,
     * Aceasta metoda poate fi apelata o singura data la initializare.

     */
    void init(Model model, View view);

    /**
     * Porneste controllerul: seteaza callback-urile View si eventual bucla de redraw
     */
    void start();

    /**
     * Opreste controllerul si curata resursele.
     */
    void stop();
}
