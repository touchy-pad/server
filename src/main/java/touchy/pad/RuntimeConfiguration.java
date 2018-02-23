package touchy.pad;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.util.ReflectionUtils;

/**
 * Marks an interface as providing access to runtime configuration.
 *
 * This allows advice to be applied to all no-arg methods returning a primitive
 * type or a string.
 *
 * @author Jan Groothuijse
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RuntimeConfiguration {

    /**
     * Utility class to inspect and reflect on configuration interfaces.
     *
     * Users of configuration, objects that depend on configuration do not need
     * to use this class, it exists to aid settings menu's, configuration
     * manipulation etc.
     *
     * @author Jan Groothuijse
     */
    interface Util {

        /**
         * @param method to get the configuration key of.
         * @return the name of the configuration directive of method.
         */
        static String getConfigName(final Method method) {
            return getConfigName(method.getDeclaringClass(), method.getName());
        }

        /**
         * Contains naming scheme.
         *
         * @param clazz the class of the method.
         * @param methodName the name of the method.
         * @return a key.
         */
        static String getConfigName(final Class<?> clazz,
                final String methodName) {
            return clazz.getName() + '.' + methodName;
        }

        /**
         * @param clazz the interface to get the configuration methods of.
         * @return the method in the clazz to be used as configuration items.
         */
        static List<Method> getConfigMethodsFor(final Class<?> clazz) {
            Predicate<Method> hasRightReturnType = method -> {
                final Class<?> returnType = method.getReturnType();
                return boolean.class.equals(returnType)
                        || int.class.equals(returnType)
                        || long.class.equals(returnType)
                        || float.class.equals(returnType)
                        || double.class.equals(returnType)
                        || String.class.equals(returnType);
            };

            return Arrays.asList(ReflectionUtils.getAllDeclaredMethods(clazz))
                    .stream()
                    .filter(method -> method
                            .getAnnotation(RuntimeConfiguration.class) != null)
                    .filter(hasRightReturnType).collect(Collectors.toList());
        }

    }
}
