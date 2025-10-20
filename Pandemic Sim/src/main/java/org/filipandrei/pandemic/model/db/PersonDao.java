package org.filipandrei.pandemic.model.db;

import kotlin.NotImplementedError;
import org.filipandrei.pandemic.model.configs.Configs;
import org.filipandrei.pandemic.model.entities.Person;

import java.util.Collection;
import java.util.List;

public class PersonDao implements DataAccessObject<Person> {
    static String dbUrl = Configs.get("db.url");

    @Override
    public boolean save(Person person) {
        throw new NotImplementedError(); // TODO
//        try (
//                Connection c = DriverManager.getConnection(dbUrl);
//                PreparedStatement stmt = c.prepareStatement(DataBaseUtils.getSqlFromScript(Paths.savePerson));
//                ) {
//            stmt.setInt(1, person.getId());
//            stmt.setInt(2, person.getSimId());
//            stmt.setInt(3, person.getPartnerId());
//            stmt.setInt(4, person.getHouseId());
//            stmt.setInt(5, person.getVehicleId());
//            stmt.setInt(6, person.getFamilyId());
//            stmt.setString(7, person.getName());
//            stmt.setString(8, person.getInfectionState().toString());
//            stmt.setString(9, person.getLifeStage().toString());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return false;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Collection<Person> getAll() {
        return List.of();
    }

    @Override
    public Person getById(int id) {
        return null;
    }
}
