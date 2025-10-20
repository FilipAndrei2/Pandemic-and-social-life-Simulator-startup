package org.filipandrei.pandemic.tests;

import org.filipandrei.pandemic.model.configs.Configs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigsTests {
    @Test
    void ReadDataBaseUrl() {
        assertEquals("jdbc:sqlite:test_database.db", Configs.get("db.url"));
    }
}
