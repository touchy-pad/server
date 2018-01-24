package touchy.pad.aspects;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import touchy.pad.RuntimeConfiguration;
import touchy.pad.RuntimeConfigurationBackend;
import touchy.pad.SpringConfig;
import touchy.pad.backend.ConstantRuntimeConfigurationBackend;

/**
 * Intergration test to make sure the RuntimeConfigurationAspect is applied to
 * RuntimeConfiguration annotated methods.
 * 
 * @author Jan Groothuijse
 */
public interface RuntimeConfigurationAspectIntegrationTest {

    // Given a plain, non-proxied instance of the config,
    /**
     * Baseline, non-adviced config to test against.
     */
    SomeConfig NAKED = new SomeConfigImpl();

    /**
     * Tests that when the backend has a value, it will be returned by the
     * config instead of the default value.
     * 
     * Key to this test is that the SomeConfig interface returns the lowest
     * value for each type and the back-end returns the lowest + 1.
     * 
     * @author Jan Groothuijse
     */
    @RunWith(SpringRunner.class)
    @ContextConfiguration(classes = SpringConfig.class)
    @ActiveProfiles("constant")
    public static class TestWithConstants {

        // given a backend that never returns null and also returns some
        // other value than the default of the config interface,
        /**
         * To see what values where adviced by the aspect.
         */
        final RuntimeConfigurationBackend advised =
                new ConstantRuntimeConfigurationBackend();

        // given a proxied instance,
        /**
         * System under test.
         */
        @Autowired
        SomeConfig someConfig;

        // when getting getting a string from the config,
        /**
         * Tests that String methods are advised.
         */
        @Test
        public void testStringInjection() {
            assertEquals("We expect the non-proxied instance to return an empty"
                    + " string.", NAKED.someString(), "");
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
                    NAKED.someBool(), false);
            assertEquals(
                    "We expect the proxied result to match the backend for"
                            + " booleans.",
                    advised.getBoolean(""), someConfig.someBool());
        }

        // when getting getting a Byte from the config,
        /**
         * Tests that Byte methods are advised.
         */
        @Test
        public void testByteInjection() {
            assertEquals("We expect the non-proxied instance to return 0.",
                    NAKED.someByte(), 0);
            assertEquals(
                    "We expect the proxied result to match the backend for"
                            + " Bytes.",
                    advised.getByte(""), (Byte) someConfig.someByte());
        }

        // when getting getting a Char from the config,
        /**
         * Tests that Char methods are advised.
         */
        @Test
        public void testCharInjection() {
            assertEquals("We expect the non-proxied instance to return 0.",
                    NAKED.someChar(), 0);
            assertEquals(
                    "We expect the proxied result to match the backend for"
                            + " Chars.",
                    advised.getChar(""), (Character) someConfig.someChar());
        }

        // when getting getting a Short from the config,
        /**
         * Tests that Short methods are advised.
         */
        @Test
        public void testShortInjection() {
            assertEquals("We expect the non-proxied instance to return 0.",
                    NAKED.someShort(), 0);
            assertEquals(
                    "We expect the proxied result to match the backend for"
                            + " Shorts.",
                    advised.getShort(""), (Short) someConfig.someShort());
        }

        // when getting getting a Int from the config,
        /**
         * Tests that Int methods are advised.
         */
        @Test
        public void testIntInjection() {
            assertEquals("We expect the non-proxied instance to return 0.",
                    NAKED.someInt(), 0);
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
                    NAKED.someLong(), 0L);
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
                    NAKED.someFloat(), 0.0f, 0.01);
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
                    NAKED.someDouble(), 0.0, 0.01);
            assertEquals(
                    "We expect the proxied result to match the backend for"
                            + " Doubles.",
                    advised.getDouble(""), (Double) someConfig.someDouble());
        }
    }

    // given a configuration backend that always returns null,
    /**
     * Tests that when the backend has no value, it will return the default
     * value.
     * 
     * @author Jan Groothuijse
     */
    @RunWith(SpringRunner.class)
    @ContextConfiguration(classes = SpringConfig.class)
    @ActiveProfiles("allwaysNull")
    public static class TestWithPassTrough {
        // given a proxied instance,
        /**
         * System under test.
         */
        @Autowired
        SomeConfig someConfig;

        // when getting getting a Boolean from the config,
        /**
         * Tests that Boolean methods are advised.
         */
        @Test
        public void testBooleanPassthrough() {
            assertEquals(
                    "We expect the non-proxied instance and the non-"
                            + "provied instance to return the same.",
                    NAKED.someBool(), someConfig.someBool());
        }

        // when getting getting a Byte from the config,
        /**
         * Tests that Byte methods are advised.
         */
        @Test
        public void testBytePassthrough() {
            assertEquals(
                    "We expect the non-proxied instance and the non-"
                            + "provied instance to return the same.",
                    NAKED.someByte(), someConfig.someByte());
        }

        // when getting getting a Char from the config,
        /**
         * Tests that Char methods are advised.
         */
        @Test
        public void testCharPassthrough() {
            assertEquals(
                    "We expect the non-proxied instance and the non-"
                            + "provied instance to return the same.",
                    NAKED.someChar(), someConfig.someChar());
        }

        // when getting getting a Short from the config,
        /**
         * Tests that Short methods are advised.
         */
        @Test
        public void testShortPassthrough() {
            assertEquals(
                    "We expect the non-proxied instance and the non-"
                            + "provied instance to return the same.",
                    NAKED.someShort(), someConfig.someShort());
        }

        // when getting getting a Integer from the config,
        /**
         * Tests that Integer methods are advised.
         */
        @Test
        public void testIntegerPassthrough() {
            assertEquals(
                    "We expect the non-proxied instance and the non-"
                            + "provied instance to return the same.",
                    NAKED.someInt(), someConfig.someInt());
        }

        // when getting getting a Long from the config,
        /**
         * Tests that Long methods are advised.
         */
        @Test
        public void testLongPassthrough() {
            assertEquals(
                    "We expect the non-proxied instance and the non-"
                            + "provied instance to return the same.",
                    NAKED.someLong(), someConfig.someLong());
        }

        // when getting getting a Float from the config,
        /**
         * Tests that Float methods are advised.
         */
        @Test
        public void testFloatPassthrough() {
            assertEquals(
                    "We expect the non-proxied instance and the non-"
                            + "provied instance to return the same.",
                    NAKED.someFloat(), someConfig.someFloat(), 0.01f);
        }

        // when getting getting a Double from the config,
        /**
         * Tests that Double methods are advised.
         */
        @Test
        public void testDoublePassthrough() {
            assertEquals(
                    "We expect the non-proxied instance and the non-"
                            + "provied instance to return the same.",
                    NAKED.someDouble(), someConfig.someDouble(), 0.01);
        }

        // when getting getting a String from the config,
        /**
         * Tests that String methods are advised.
         */
        @Test
        public void testStringPassthrough() {
            assertEquals(
                    "We expect the non-proxied instance and the non-"
                            + "provied instance to return the same.",
                    NAKED.someString(), someConfig.someString());
        }
    }

    /**
     * Implementation so that a SomeConfig can be instantiated.
     * 
     * @author Jan Groothuijse
     */
    @Component
    public static class SomeConfigImpl implements SomeConfig {

        // given a proxied instance,
        /**
         * System under test.
         */
        @Autowired
        SomeConfig someConfig;
    }

    /**
     * Toy config to test all primitives.
     * 
     * @author Jan Groothuijse
     */
    public static interface SomeConfig {

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
}
