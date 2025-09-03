package com.oneaura.cpscounter.configlib;

/**
 * A base class for creating a configuration file.
 * Your mod's config class should extend this class.
 *
 * <p>Configuration options should be declared as public, non-final fields.
 * For example:
 * <pre>{@code
 * public class MyModConfig extends ConfigHolder {
 * @Comment("The message to display on startup.")
 * public String startupMessage = "Hello World!";
 *
 * @Comment("The number of items to spawn.")
 * public int spawnCount = 10;
 * }
 * }</pre>
 *
 * Use the {@link Comment} annotation to add descriptive comments to your config fields,
 * which can be used by config screens or documentation generators.
 */
public abstract class ConfigHolder {
    // This class is intentionally left empty. It serves as a marker
    // for the configuration system to identify config classes.
}
