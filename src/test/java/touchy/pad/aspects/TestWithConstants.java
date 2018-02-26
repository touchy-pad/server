package touchy.pad.aspects;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import touchy.pad.RuntimeConfigurationBackend;
import touchy.pad.SomeConfig;
import touchy.pad.SpringConfig;
import touchy.pad.backend.ConstantRuntimeConfigurationBackend;

/**
 * Tests that when the backend has a value, it will be returned by the config
 * instead of the default value.
 *
 * Key to this test is that the SomeConfig interface returns the lowest value
 * for each type and the back-end returns the lowest + 1.
 *
 * @author Jan Groothuijse
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
@ActiveProfiles({ "constant", "noTouchLink" })
public final class TestWithConstants {

    // given a backend that never returns null and also returns some
    // other value than the default of the config interface,
    /**
     * To see what values where adviced by the aspect.
     */
    private final RuntimeConfigurationBackend advised =
            new ConstantRuntimeConfigurationBackend();

    // given a proxied instance,
    /**
     * System under test.
     */
    @Autowired
    private SomeConfig someConfig;

    /**
     * Margin of error for float comparison.
     */
    public static final float EPSILON = 0.01f;

    // Given a plain, non-proxied instance of the config,
    /**
     * Baseline, non-adviced config to test against.
     */
    public static final SomeConfig NAKED = new SomeConfigImpl();

    // when getting getting a string from the config,
    /**
     * Tests that String methods are advised.
     */
    @Test
    public void testStringInjection() {
        assertEquals("We expect the non-proxied instance to return an empty"
                + " string.", TestWithConstants.NAKED.someString(), "");
        assertEquals(
                "We expect the proxied result to match the backend for"
                        + " strings.",
                advised.getString(""), someConfig.someString());
    }

    // when getting getting a boolean from the config,
    /**
     * Tests that bool methods are advised.
     */
    @Test
    public void testBooleanInjection() {
        assertEquals("We expect the non-proxied instance to return false.",
                TestWithConstants.NAKED.someBool(), false);
        assertEquals(
                "We expect the proxied result to match the backend for"
                        + " booleans.",
                advised.getBoolean(""), someConfig.someBool());
    }

    // when getting getting a Int from the config,
    /**
     * Tests that Int methods are advised.
     */
    @Test
    public void testIntInjection() {
        assertEquals("We expect the non-proxied instance to return 0.",
                TestWithConstants.NAKED.someInt(), 0);
        assertEquals(
                "We expect the proxied result to match the backend for"
                        + " Ints.",
                advised.getInteger(""), (Integer) someConfig.someInt());
    }

    // when getting getting a Long from the config,
    /**
     * Tests that Long methods are advised.
     */
    @Test
    public void testLongInjection() {
        assertEquals("We expect the non-proxied instance to return 0.",
                TestWithConstants.NAKED.someLong(), 0L);
        assertEquals(
                "We expect the proxied result to match the backend for"
                        + " Longs.",
                advised.getLong(""), (Long) someConfig.someLong());
    }

    // when getting getting a Float from the config,
    /**
     * Tests that Float methods are advised.
     */
    @Test
    public void testFloatInjection() {
        assertEquals("We expect the non-proxied instance to return 0.",
                TestWithConstants.NAKED.someFloat(), 0.0f,
                TestWithConstants.EPSILON);
        assertEquals(
                "We expect the proxied result to match the backend for"
                        + " Floats.",
                advised.getFloat(""), (Float) someConfig.someFloat());
    }

    // when getting getting a Double from the config,
    /**
     * Tests that Double methods are advised.
     */
    @Test
    public void testDoubleInjection() {
        assertEquals("We expect the non-proxied instance to return 0.",
                TestWithConstants.NAKED.someDouble(), 0.0,
                TestWithConstants.EPSILON);
        assertEquals(
                "We expect the proxied result to match the backend for"
                        + " Doubles.",
                advised.getDouble(""), (Double) someConfig.someDouble());
    }
}
