package touchy.pad;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test of the RuntimeConfiguration.Util class.
 *
 * @author Jan Groothuijse
 */
public final class ConfigReflectionTest {

    /**
     * Checks the number of methods in SomeConfig.
     */
    @Test
    public void checkMethodNumber() {
        final int expectedMethods = 6;
        assertEquals("SomeConfig has 6 methods for configuration.",
                expectedMethods, RuntimeConfiguration.Util
                        .getConfigMethodsFor(SomeConfig.class).size());
    }
}
