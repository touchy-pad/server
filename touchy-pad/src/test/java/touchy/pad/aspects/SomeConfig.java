package touchy.pad.aspects;

import touchy.pad.RuntimeConfiguration;

/**
 * Toy config to test all primitives.
 *
 * @author Jan Groothuijse
 */
public interface SomeConfig {

    /**
     * @return a boolean.
     */
    @RuntimeConfiguration
    default boolean someBool() {
        return false;
    }

    /**
     * @return a byte.
     */
    @RuntimeConfiguration
    default byte someByte() {
        return 0;
    }

    /**
     * @return a char.
     */
    @RuntimeConfiguration
    default char someChar() {
        return 0;
    }

    /**
     * @return a short.
     */
    @RuntimeConfiguration
    default short someShort() {
        return 0;
    }

    /**
     * @return a int.
     */
    @RuntimeConfiguration
    default int someInt() {
        return 0;
    }

    /**
     * @return a long.
     */
    @RuntimeConfiguration
    default long someLong() {
        return 0L;
    }

    /**
     * @return a string.
     */
    @RuntimeConfiguration
    default String someString() {
        return "";
    }

    /**
     * @return a float.
     */
    @RuntimeConfiguration
    default float someFloat() {
        return 0.0f;
    }

    /**
     * @return a double.
     */
    @RuntimeConfiguration
    default double someDouble() {
        return 0.0;
    }
}
