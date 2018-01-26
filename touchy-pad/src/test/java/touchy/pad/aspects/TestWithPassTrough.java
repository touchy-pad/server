package touchy.pad.aspects;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import touchy.pad.SpringConfig;

// given a configuration backend that always returns null,
/**
 * Tests that when the backend has no value, it will return the default value.
 *
 * @author Jan Groothuijse
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
@ActiveProfiles("allwaysNull")
public final class TestWithPassTrough {
    // given a proxied instance,
    /**
     * System under test.
     */
    @Autowired
    private SomeConfig someConfig;

    // when getting getting a Boolean from the config,
    /**
     * Tests that Boolean methods are advised.
     */
    @Test
    public void testBooleanPassthrough() {
        assertEquals(
                "We expect the non-proxied instance and the non-"
                        + "provied instance to return the same.",
                TestWithConstants.NAKED.someBool(), someConfig.someBool());
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
                TestWithConstants.NAKED.someByte(), someConfig.someByte());
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
                TestWithConstants.NAKED.someChar(), someConfig.someChar());
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
                TestWithConstants.NAKED.someShort(), someConfig.someShort());
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
                TestWithConstants.NAKED.someInt(), someConfig.someInt());
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
                TestWithConstants.NAKED.someLong(), someConfig.someLong());
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
                TestWithConstants.NAKED.someFloat(), someConfig.someFloat(),
                TestWithConstants.EPSILON);
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
                TestWithConstants.NAKED.someDouble(), someConfig.someDouble(),
                TestWithConstants.EPSILON);
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
                TestWithConstants.NAKED.someString(), someConfig.someString());
    }
}
