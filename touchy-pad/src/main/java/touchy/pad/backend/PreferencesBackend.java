package touchy.pad.backend;

import java.util.function.BiFunction;
import java.util.prefs.Preferences;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import touchy.pad.RuntimeConfigurationBackend;

/**
 * Using Preferences API to implement a runtime configuration backend.
 * 
 * @author Jan Groothuijse
 */
@Component
@Profile("preferences")
public final class PreferencesBackend implements RuntimeConfigurationBackend {

    /**
     * Repository of configuration.
     */
    final private Preferences preferences;

    /**
     * Constructor to be called by DI framework.
     * 
     * @param name of the node.
     */
    PreferencesBackend(final @Value(
            value = "$preferences.key : 'touchy-pad'") String name) {
        preferences = Preferences.userRoot().node(name);
    }

    /**
     * @param <E> the return type
     * @param name the key of the configuration.
     * @param defaultValue default value of the configuration.
     * @param fun function to get the value from the configuration.
     * @return the configured value.
     */
    <E> E getValue(final String name, final E defaultValue,
            final BiFunction<String, E, E> fun) {
        if (preferences.get(name, null) != null) {
            return fun.apply(name, defaultValue);
        } else {
            return null;
        }
    }

    @Override
    public Boolean getBoolean(final String name) {
        return getValue(name, true, preferences::getBoolean);
    }

    @Override
    public Integer getInteger(final String name) {
        return getValue(name, 0, preferences::getInt);
    }

    @Override
    public Long getLong(final String name) {
        return getValue(name, 0L, preferences::getLong);
    }

    @Override
    public Float getFloat(final String name) {
        return getValue(name, 0.0f, preferences::getFloat);
    }

    @Override
    public Double getDouble(final String name) {
        return getValue(name, 0.0, preferences::getDouble);
    }

    @Override
    public String getString(final String name) {
        return getValue(name, "", preferences::get);
    }

    @Override
    public void setBoolean(final String key, final boolean value) {
        preferences.putBoolean(key, value);
    }

    @Override
    public void setInt(final String key, final int value) {
        preferences.putInt(key, value);
    }

    @Override
    public void setLong(final String key, final long value) {
        preferences.putLong(key, value);
    }

    @Override
    public void setFloat(final String key, final float value) {
        preferences.putFloat(key, value);
    }

    @Override
    public void setDouble(final String key, final double value) {
        preferences.putDouble(key, value);
    }

    @Override
    public void setString(final String key, final String value) {
        preferences.put(key, value);
    }

    /**
     * Removes a value.
     * 
     * @param key of the configu directive to remove.
     */
    public void remove(final String key) {
        preferences.remove(key);
    }
}
