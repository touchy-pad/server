package touchy.pad.aspects;

import java.util.function.Function;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import touchy.pad.RuntimeConfiguration.Util;
import touchy.pad.RuntimeConfigurationBackend;

/**
 * Advices runtime configuration objects on how to obtain values from a runtime
 * configuration store (database, registry, preferences file, online account).
 *
 * Can only advice on primitives and string.
 *
 * Defines the naming scheme, mapping Config class and method names to
 * configuration directive names.
 *
 * Acts as a layer of indirection, seperating the definition of the config
 * interfaces from the storage of the config values.
 *
 * @author Jan Groothuijse
 */
@Component
@Aspect
@RequiredArgsConstructor
public final class RuntimeConfigurationAspect {
    /**
     * Place that saves the actual values of the configuration.
     */
    private final RuntimeConfigurationBackend backEnd;

    /**
     * @param <E> type of the return.
     * @param joinPoint to get the name from.
     * @param getValue callback to get a value of the correct type.
     * @return the configured value, which may come from the backend or default
     *         to the
     * @throws Throwable if the original method throws.
     */
    @SuppressWarnings("unchecked")
    <E> E getConfiguredOrDefault(final ProceedingJoinPoint joinPoint,
            final Function<String, E> getValue) throws Throwable {
        final Signature methodSignature = joinPoint.getSignature();
        // Naming scheme:
        final String name;
        name = Util.getConfigName(methodSignature.getDeclaringType(),
                methodSignature.getName());

        final E configured = getValue.apply(name);
        if (configured == null) {
            return (E) joinPoint.proceed();
        } else {
            return configured;
        }
    }

    /**
     * @param joinPoint to get the name of
     * @return the final value for this configuration.
     * @throws Throwable when something went wrong.
     */
    @Around("execution(@touchy.pad.RuntimeConfiguration boolean *..*.*())")
    public Boolean booleanMethod(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        return getConfiguredOrDefault(joinPoint, backEnd::getBoolean);
    }

    /**
     * @param joinPoint to get the name of
     * @return the final value for this configuration.
     * @throws Throwable when something went wrong.
     */
    @Around("execution(@touchy.pad.RuntimeConfiguration int *..*.*())")
    public Integer intMethod(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        return getConfiguredOrDefault(joinPoint, backEnd::getInteger);
    }

    /**
     * @param joinPoint to get the name of
     * @return the final value for this configuration.
     * @throws Throwable when something went wrong.
     */
    @Around("execution(@touchy.pad.RuntimeConfiguration long *..*.*())")
    public Long longMethod(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        return getConfiguredOrDefault(joinPoint, backEnd::getLong);
    }

    /**
     * @param joinPoint to get the name of
     * @return the final value for this configuration.
     * @throws Throwable when something went wrong.
     */
    @Around("execution(@touchy.pad.RuntimeConfiguration float *..*.*())")
    public Float floatMethod(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        return getConfiguredOrDefault(joinPoint, backEnd::getFloat);
    }

    /**
     * @param joinPoint to get the name of
     * @return the final value for this configuration.
     * @throws Throwable when something went wrong.
     */
    @Around("execution(@touchy.pad.RuntimeConfiguration double *..*.*())")
    public Double doubleMethod(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        return getConfiguredOrDefault(joinPoint, backEnd::getDouble);
    }

    /**
     * @param joinPoint to get the name of
     * @return the final value for this configuration.
     * @throws Throwable when something went wrong.
     */
    @Around("execution(@touchy.pad.RuntimeConfiguration String *..*.*())")
    public String stringMethod(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        return getConfiguredOrDefault(joinPoint, backEnd::getString);
    }
}
