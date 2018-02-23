package touchy.pad.backend;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests configuration backend using java.util.preferences.
 *
 * @author Jan Groothuijse
 */
public final class PreferencesBackendUnitTest {

    /**
     * Margin for float and double comparisons.
     */
    private static final double MARGIN = 0.001;

    /**
     * System under test, used by all methods.
     */
    private final PreferencesBackend sut = new PreferencesBackend("someName");

    /**
     * Checks the functioning of boolean storage.
     */
    @Test
    public void checkBool() {
        final String key = "boolean";
        sut.remove(key);
        assertEquals(null, sut.getBoolean(key));
        sut.setBoolean(key, true);
        assertEquals(true, sut.getBoolean(key));
    }

    /**
     * Checks the functioning of boolean storage.
     */
    @Test
    public void checkInt() {
        final String key = "integer";
        sut.remove(key);
        assertEquals(null, sut.getInteger(key));
        sut.setInt(key, 0);
        assertEquals(0, (int) sut.getInteger(key));
    }

    /**
     * Checks the functioning of boolean storage.
     */
    @Test
    public void checkLong() {
        final String key = "long";
        sut.remove(key);
        assertEquals(null, sut.getLong(key));
        sut.setLong(key, 0);
        assertEquals(0, (long) sut.getLong(key));
    }

    /**
     * Checks the functioning of boolean storage.
     */
    @Test
    public void checkFloat() {
        final String key = "float";
        sut.remove(key);
        assertEquals(null, sut.getFloat(key));
        sut.setFloat(key, 0);
        assertEquals(0.0f, (float) sut.getFloat(key), MARGIN);
    }

    /**
     * Checks the functioning of boolean storage.
     */
    @Test
    public void checkDouble() {
        final String key = "double";
        sut.remove(key);
        assertEquals(null, sut.getDouble(key));
        sut.setDouble(key, 0);
        assertEquals(0.0, (double) sut.getDouble(key), MARGIN);
    }

    /**
     * Checks the functioning of boolean storage.
     */
    @Test
    public void checkString() {
        final String key = "string";
        sut.remove(key);
        assertEquals(null, sut.getDouble(key));
        sut.setString(key, "");
        assertEquals("", sut.getString(key));
    }
}
