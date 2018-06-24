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

    /**
     * Tests method based names.
     *
     * @throws NoSuchMethodException if the method name is not correct
     * @throws SecurityException if the method is not visibible
     */
    @Test
    public void testUtil() throws NoSuchMethodException, SecurityException {
        assertEquals("touchy.pad.ConfigReflectionTest.testUtil",
                RuntimeConfiguration.Util
                        .getConfigName(this.getClass().getMethod("testUtil")));
    }
}
