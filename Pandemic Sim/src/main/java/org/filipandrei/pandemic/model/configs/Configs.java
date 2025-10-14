package org.filipandrei.pandemic.model.configs;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Static class responsible for managing the application's configuration file.
 *
 * <p>This class handles reading and writing configuration values
 * from a predefined file in the classpath (see {@link #CONFIG_FILE}).</p>
 */
public final class Configs {

    /** The name of the configuration file in resources. */
    private static final String CONFIG_FILE = "config.properties";

    /** Properties object holding all configuration key-value pairs. */
    private static final Properties properties = new Properties();

    // Static block to load config when class is first used
    static {
        try (InputStream input = Configs.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("Warning: Could not load config file from classpath: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Private constructor to prevent instantiation. */
    private Configs() {}

    /** Returns the value of a configuration property. */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /** Sets a configuration property and immediately writes it to disk. */
    public static void set(String key, String value) {
        properties.setProperty(key, value);
        save();
    }

    /** Saves the current properties to the file. */
    private static void save() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Application configuration");
        } catch (IOException e) {
            System.err.println("Error: Could not save config file to " + CONFIG_FILE);
        }
    }
}
