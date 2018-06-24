package touchy.pad.backend;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import touchy.pad.RuntimeConfigurationBackend;

/**
 * Implementation that returns constant values regardless of the name.
 *
 * @author Jan Groothuijse
 */
@Profile("constant")
@Component
public final class ConstantRuntimeConfigurationBackend
        implements RuntimeConfigurationBackend {

    @Override
    public Boolean getBoolean(final String name) {
        return true;
    }

    @Override
    public Integer getInteger(final String name) {
        return 1;
    }

    @Override
    public Long getLong(final String name) {
        return 1L;
    }

    @Override
    public Float getFloat(final String name) {
        return 1.0f;
    }

    @Override
    public Double getDouble(final String name) {
        return 1.0;
    }

    @Override
    public String getString(final String name) {
        return "string";
    }

    @Override
    public void setBoolean(final String name, final boolean value) {
        // Nothing to set.
    }

    @Override
    public void setInt(final String name, final int value) {
        // Nothing to set.
    }

    @Override
    public void setLong(final String name, final long value) {
        // Nothing to set.
    }

    @Override
    public void setFloat(final String name, final float value) {
        // Nothing to set.
    }

    @Override
    public void setDouble(final String name, final double value) {
        // Nothing to set.
    }

    @Override
    public void setString(final String name, final String value) {
        // Nothing to set.
    }
}
