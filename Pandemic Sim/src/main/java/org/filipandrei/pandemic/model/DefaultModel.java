package org.filipandrei.pandemic.model;

import org.filipandrei.pandemic.model.db.SimulationDao;
import org.filipandrei.pandemic.model.db.UpdateDataBaseFail;
import org.filipandrei.pandemic.model.entities.ReadOnlySimulation;
import org.filipandrei.pandemic.model.entities.Simulation;

import java.util.Collection;
import java.util.Optional;

public class DefaultModel implements Model {

    private Simulation activeSim = null;

    @Override
    public void createAndReadActiveSimulation(String name) {
        var dao = new SimulationDao();
        int id = dao.createNewSimulation(name);
        if (id == -1) {
            throw new RuntimeException("Could not create new simulation with name " + name);
        }
        activeSim = new Simulation(id, name);
    }

    @Override
    public boolean readActiveSimulation(int simId) {
        if (activeSim != null) {
            activeSim = null;
        }
        var dao = new SimulationDao();
        activeSim = dao.getById(simId);
        return activeSim != null;
    }

    @Override
    public boolean updateActiveSimulation() {
        if (activeSim == null) {
            return false;
        }
        var dao = new SimulationDao();
        return dao.save(this.activeSim);
    }

    @Override
    public void deleteSimulation(int simId) {
        new SimulationDao().deleteById(simId);
    }

    @Override
    public Optional<Integer> getActiveSimulationId() {
        return activeSim != null ? Optional.of(activeSim.getId()) : Optional.empty();
    }

    @Override
    public ReadOnlySimulation getReadOnlySimulation() {
        return activeSim;
    }

    @Override
    public void setActiveSimulationName(String newName) {
        if (activeSim == null) {
            return;
        }
        activeSim.setName(newName);
        try {
            if (!new SimulationDao().setNameToId(activeSim.getId(), newName)) { // TODO: foloseste apiul propriu pt concurenta in bd
                throw new UpdateDataBaseFail("Could not update entry row name");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void tick() {
        this.activeSim.update();
    }

    @Override
    public Collection<String> getSimulationsNames() {
        return new SimulationDao()
                .getAll()
                .stream()
                .map(Simulation::getName)
                .toList();
    }

    @Override
    public void endAndSaveActiveSim() {
        if (activeSim != null) {
            new SimulationDao().save(activeSim);
        }
    }
}
