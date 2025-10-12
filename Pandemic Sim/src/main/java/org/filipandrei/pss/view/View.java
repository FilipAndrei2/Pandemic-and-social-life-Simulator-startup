package org.filipandrei.pss.view;

import org.filipandrei.pss.model.dtos.ReadOnlyWorld;

/**
 * Interfata componentei view din arhitectura MVC. View este gandita sa ruleze pe un thread separat.
 */
public interface View {

    /**
     * Deseneaza starea curenta a lumii
     */
    void drawWorld(ReadOnlyWorld world);

    /**
     * Seteaza callback pentru cand utilizatorul apasa Start
     */
    void setOnStart(Runnable callback);

    /**
     * Seteaza callback pentru cand utilizatorul apasa Stop
     */
    void setOnStop(Runnable callback);

    /**
     * Seteaza callback pentru cand utilizatorul apasa Step
     */
    void setOnStep(Runnable callback);

    /**
     * Notifica utilizatorul ca simularea a pornit
     */
    void showSimulationRunning();

    /**
     * Notifica utilizatorul ca simularea s-a oprit
     */
    void showSimulationStopped();
}
