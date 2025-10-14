package org.filipandrei.pandemic.model.entities;

/**
 * Marker annotation indicating that a field should be persisted in the database.
 *
 * <p>Fields annotated with {@code @Persisted} are stored and loaded from the database
 * because they cannot be dynamically calculated.</p>
 */
public @interface Persisted {
}
