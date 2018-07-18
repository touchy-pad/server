package touchy.pad.settings;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

import lombok.Getter;
import touchy.pad.RuntimeConfiguration;
import touchy.pad.RuntimeConfigurationBackend;

/**
 * Some user runtime configuration item.
 *
 * @author Jan Groothuijse
 */
@Getter
public class Setting {
    /**
     * Message to look up name and description in the message files.
     */
    private final String message;

    /**
     * Sets a new value for this setting.
     */
    private final BiConsumer<RuntimeConfigurationBackend, Object> setter;

    /**
     * Pointer to the method of this setting.
     */
    private final Method method;

    Setting(final Method m) {
        method = m;
        final Class<?> returnType = m.getReturnType();
        message = m.getAnnotation(RuntimeConfiguration.class).value();
        final String name = RuntimeConfiguration.Util
                .getConfigName(m.getDeclaringClass(), m.getName());
        if (returnType == int.class) {
            setter = (backEnd, value) -> backEnd.setInt(name, (int) value);
        } else if (returnType == boolean.class) {
            setter = (backEnd, value) -> backEnd.setBoolean(name,
                    (boolean) value);
        } else if (returnType == long.class) {
            setter = (backEnd, value) -> backEnd.setLong(name, (long) value);
        } else if (returnType == float.class) {
            setter = (backEnd, value) -> backEnd.setFloat(name, (float) value);
        } else if (returnType == double.class) {
            setter = (backEnd, value) -> backEnd.setDouble(name,
                    (double) value);
        } else {
            setter = (backEnd, value) -> backEnd.setString(name,
                    (String) value);
        }
    }
}
