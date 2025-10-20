package org.filipandrei.pandemic.model.db;

import org.filipandrei.pandemic.model.entities.Entity;

import java.util.Collection;

/**
 * A generic Data Access Object (DAO) interface for performing CRUD operations
 * on {@link Entity} domain objects in the database.
 */
public interface DataAccessObject<E extends Entity> {

    /**
     * Saves a {@link Entity} to the database.
     *
     * @param e the entity to add
     * @return true if entity was succesfully saved, false on saving failure
     */
    boolean save(E e);

    /**
     * Deletes an {@link Entity} from the database by its ID.
     * <p>
     * If no entity exists with the specified ID, the operation has no effect.
     * </p>
     *
     * @param id the ID of the entity to delete
     */
    void deleteById(int id);

    /**
     * Retrieves all {@link Entity} objects from the database.
     *
     * @return a list of all entities stored in the database
     */
    Collection<E> getAll();

    /**
     * Retrieves an {@link Entity} domain object from the database by its ID.
     * <p>
     * If no entity exists with the specified {@code id}, this method returns {@code null}.
     * </p>
     *
     * @param id the ID of the entity to retrieve from the database
     * @return the {@link Entity} object corresponding to the given ID,
     *         or {@code null} if no such entity exists in the database
     */
    E getById(int id);
}
