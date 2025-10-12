package org.filipandrei.pss.model;

import org.filipandrei.pss.model.entities.Entity;
import org.filipandrei.pss.model.entities.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public abstract class SimulationModel {
    /**
     * Initializes the model. Call before you start to use the model.
     */
    public abstract void createModel(String simName);

    /**
     * Destroys the model, leaving it in an undefined state.
     */
    public abstract void destroyModel();

    /**
     * Registers one entity. Make sure to call createModel() before you start adding data.
     * @param e Entity to be registered.
     * @return true on success, false if you can't add entity.
     */
    public abstract boolean addEntity(@NotNull Entity e);

    /**
     * Registers a batch of entities. Make sure to call createModel() before you start adding data.
     * @param entities Entities to be registered
     * @return true on succes, false otherwise
     */
    public abstract boolean addEntities(Collection<Entity> entities);

    /**
     * Starts a not started/paused simulation. If the simulation is already started, the call has no effect.
     */
    public abstract void startSimulation();

    /**
     * Pauses the simulation. If the simulation is already paused, the call has no effect.
     */
    public abstract void pauseSimulation();

    /**
     * Kills the current simulation, cleaning all the data. Make sure to save a snapshot before killing if you want to load it in the future.
     */
    public abstract void killSimulation();

    /**
     *
     */

    /**
     * Loads a saved simulation state from the db.
     * @param simId The id of the simulation to be loaded
     * @return true if the coresponding sim is in the db, false on failure
     */
    public abstract boolean loadSimulation(int simId);

    /**
     * Saves the simulation to the db.
     */
    public abstract void saveSimulation(String dbUrl);

    /**
     * Calls all the subsystems to process one step of processing.
     */
    public abstract void tick();

    /**
     * Getter for simulation info. Make sure to use only for reading!
     * @return An object that contains all the data of the simulation.
     */
    public abstract World getWorld();
}
