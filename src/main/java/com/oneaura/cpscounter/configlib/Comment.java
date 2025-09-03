package com.oneaura.cpscounter.configlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to add a descriptive comment to a configuration field.
 * This can be used by external tools, such as a config screen GUI, to show
 * helpful information to the user.
 *
 * <p>Example usage:
 * <pre>{@code
 * @Comment("The number of unicorns to spawn.")
 * public int unicornCount = 5;
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Comment {
    /**
     * The comment string. Supports multiple lines.
     * @return The descriptive comment for the config option.
     */
    String value();
}
