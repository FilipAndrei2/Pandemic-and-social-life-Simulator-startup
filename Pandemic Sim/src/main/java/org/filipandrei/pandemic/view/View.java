package org.filipandrei.pandemic.view;

import org.filipandrei.pandemic.model.entities.ReadOnlySimulation;

import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Interfata componentei view din arhitectura MVC. View este gandita sa ruleze pe un thread separat.
 */
public interface View {

    void displayMainMenu();
    void displaySimulation();
    void showPauseMenu();
    void hidePauseMenu();

    void setKeyListener(KeyListener kl);
    void setOnSimulationsNamesRequested(Callable<Collection<String>> provider);
    void setOnSimulationDataRequested(Callable<ReadOnlySimulation> provider);

    /**
     *
     * @param listener callbackul care este chemat cand se trece din ecranul de meniu in ecranul de simulare.
     */
    void setOnSimulationStart(Runnable listener);
    void setOnSimulationPause(Runnable listener);
    void setOnSimulationStop(Runnable listener);
    void setOnSimulationLoadByIdRequested(Consumer<Integer> cbLoadSim);
    void setOnProgramTerminationRequest(Runnable cbProgramTerminationHandler);
    void setOnSimulationResume(Runnable listener);
    void setOnSimulationSaveRequested(Runnable listener);
    void setOnSimulationDeleteByIdRequest(Consumer<Integer> cbDeleteSimulation);

}
