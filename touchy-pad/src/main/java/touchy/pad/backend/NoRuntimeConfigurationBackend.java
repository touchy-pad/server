package touchy.pad.backend;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import touchy.pad.RuntimeConfigurationBackend;

/**
 * No-op implementation.
 * 
 * @author Jan Groothuijse
 */
@Profile("allwaysNull")
@Component
public class NoRuntimeConfigurationBackend 
		implements RuntimeConfigurationBackend {

	@Override
	public Boolean getBoolean(final String name) {
		return null;
	}

	@Override
	public Byte getByte(final String name) {
		return null;
	}

	@Override
	public Character getChar(final String name) {
		return null;
	}

	@Override
	public Integer getInteger(final String name) {
		return null;
	}

	@Override
	public Long getLong(final String name) {
		return null;
	}

	@Override
	public Float getFloat(final String name) {
		return null;
	}

	@Override
	public Double getDouble(final String name) {
		return null;
	}

	@Override
	public String getString(final String name) {
		return null;
	}

	@Override
	public Short getShort(final String name) {
		return null;
	}
}
