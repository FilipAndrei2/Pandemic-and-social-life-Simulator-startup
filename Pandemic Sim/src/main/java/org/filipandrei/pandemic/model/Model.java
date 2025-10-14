package org.filipandrei.pandemic.model;


import org.filipandrei.pandemic.model.entities.Entity;
import org.filipandrei.pandemic.model.entities.Simulation;

import java.util.Collection;
import java.util.Optional;

public interface Model {

    Simulation getReadOnlyWorld();

    /**
     * Creaza o noua lume activa.
     * @param name Numele noii lumi
     * @param config Descriptorul simularii, un obiect care contine informatii despre nr de persoane, nr de infectati initiali, rata de infectare etc.
     * @return true daca s-a reusit crearea si setarea lumii, altfel fals
     */
    boolean createAndSetActiveWorld(String name, SimulationConfig config);

    /**
     * Incarca o lume existenta dupa ID.
     * @param simId Id-ul lumii
     * @return true daca a fost gasita si incarcata, altfel fals
     */
    boolean loadActiveWorld(int simId);

    /**
     * Salveaza lumea curenta.
     * @return true daca s-a reusit salvarea, altfel fals
     */
    boolean saveActiveWorld();

    /**
     * Sterge o lume dupa ID.
     * @param simId Id-ul lumii
     * @return true daca s-a reusit stergerea, altfel fals
     */
    boolean deleteWorld(int simId);

    /**
     * Returneaza ID-ul lumii active daca exista.
     * @return Optional cu ID-ul lumii active sau empty daca nu exista
     */
    Optional<Integer> getActiveWorldId();

    /**
     * Schimba numele lumii active.
     * @param newName Noul nume
     */
    void setActiveWorldName(String newName);

    /**
     * Adauga o entitate in lumea activa.
     * @param e Entitatea de adaugat
     * @return true daca s-a reusit adaugarea, altfel fals
     */
    boolean addEntity(Entity e);

    /**
     * Adauga un set de entitati in lumea activa.
     * @param e Colectia de entitati
     * @return true daca s-au adaugat, altfel fals
     */
    boolean addEntities(Collection<Entity> e);

    /**
     * Porneste simularea pe un thread separat.
     */
    void startSimulation();

    /**
     * Opreste simularea si lasa thread-ul sa se inchida curat.
     */
    void stopSimulation();

    /**
     * Verifica daca simularea este activa.
     * @return true daca threadul de simulare ruleaza, altfel false
     */
    boolean isSimulationRunning();
}