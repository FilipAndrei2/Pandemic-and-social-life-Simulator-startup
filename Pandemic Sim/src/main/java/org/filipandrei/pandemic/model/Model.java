package org.filipandrei.pandemic.model;


import org.filipandrei.pandemic.model.entities.ReadOnlySimulation;
import org.filipandrei.pandemic.model.entities.Simulation;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents the core model of the pandemic simulation, responsible for managing simulations
 * and their entities. Provides methods to create, load, save, delete, and manipulate
 * simulations, as well as to control the execution of the simulation loop.
 */
public interface Model {

    /**
     * Creates a new active simulation world with the specified name and sets it as active.
     * @param name the name of the new simulation world
     */
    void createAndReadActiveSimulation(String name);

    /**
     * Loads an existing simulation world by its ID and sets it as active.
     *
     * @param simId the ID of the simulation world to load
     * @return {@code true} if the world was successfully found and loaded, {@code false} otherwise
     */
    boolean readActiveSimulation(int simId);

    /**
     * Saves the current active simulation world.
     * Impl detail: Poti primi beneficii daca incerci sa faci salvarea in bd pe un alt thread virtual.
     * @return {@code true} if the world was successfully saved, {@code false} otherwise
     */
    boolean updateActiveSimulation();

    /**
     * Deletes a simulation world identified by its ID.
     *
     * @param simId the ID of the simulation world to delete
     */
    void deleteSimulation(int simId);

    /**
     * Returns the ID of the currently active simulation world, if present.
     *
     * @return an {@link Optional} containing the active simulation ID, or empty if no world is active
     */
    Optional<Integer> getActiveSimulationId();

    /**
     * Returns a read-only view of the current simulation world.
     *
     * @return the {@link Simulation} representing the current world in a read-only manner, or {@code null} if no active simulation is set
     */
    ReadOnlySimulation getReadOnlySimulation();

    /**
     * Changes the name of the currently active simulation world.
     *
     * @param newName the new name to assign to the active simulation
     */
    void setActiveSimulationName(String newName);

    /**
     * Updates current simulation for one tick.
     * The control of the main loop is designated to the Controller.
     */
    void tick();

    /**
     *
     * @return A collection with the names of the simulations available in the database, an empty collection if no simulations are in the db or if the db does not exist.
     */
    Collection<String> getSimulationsNames();

    void endAndSaveActiveSim();

}