package touchy.pad.aspects;

import java.util.function.Function;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import touchy.pad.RuntimeConfigurationBackend;

/**
 * Advices runtime configuration objects on how to obtain values from a 
 * runtime configuration store (database, registry, preferences file, online
 * account).
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
public class RuntimeConfigurationAspect {
	/**
	 * Place that saves the actual values of the configuration.
	 */
	private final RuntimeConfigurationBackend backEnd;
	
	/**
	 * Contains naming scheme.
	 * @param joinPoint
	 * @return the configured value, which may come from the backend or default
	 *         to the 
	 * @throws Throwable if the orinal mehtod throws.
	 */
	@SuppressWarnings("unchecked")
	<E> E getConfiguredOrDefault(final ProceedingJoinPoint joinPoint,
			final Function<String, E> getValue)
			throws Throwable {
		final Signature methodSignature = joinPoint.getSignature();
		// Naming scheme:
		final String name = methodSignature.getDeclaringType().getName() + "." +
				methodSignature.getName();
		
		final E configured = getValue.apply(name);
		if (configured == null) {
			return (E) joinPoint.proceed();
		} else {
			return configured;
		}
	}
	
	@Around("execution(@touchy.pad.RuntimeConfiguration boolean *..*.*())")
	public Boolean booleanMethod(final ProceedingJoinPoint joinPoint) 
			throws Throwable {
		return getConfiguredOrDefault(joinPoint, backEnd::getBoolean);
	}
	
	@Around("execution(@touchy.pad.RuntimeConfiguration byte *..*.*())")
	public Byte byteMethod(final ProceedingJoinPoint joinPoint) 
			throws Throwable {
		return getConfiguredOrDefault(joinPoint, backEnd::getByte);
	}
	
	@Around("execution(@touchy.pad.RuntimeConfiguration char *..*.*())")
	public Character charMethod(final ProceedingJoinPoint joinPoint) 
			throws Throwable {
		return getConfiguredOrDefault(joinPoint, backEnd::getChar);
	}
	
	@Around("execution(@touchy.pad.RuntimeConfiguration short *..*.*())")
	public Short shortMethod(final ProceedingJoinPoint joinPoint) 
			throws Throwable {
		return getConfiguredOrDefault(joinPoint, backEnd::getShort);
	}
	
	@Around("execution(@touchy.pad.RuntimeConfiguration int *..*.*())")
	public Integer intMethod(final ProceedingJoinPoint joinPoint) 
			throws Throwable {
		return getConfiguredOrDefault(joinPoint, backEnd::getInteger);
	}
	
	@Around("execution(@touchy.pad.RuntimeConfiguration long *..*.*())")
	public Long longMethod(final ProceedingJoinPoint joinPoint) 
			throws Throwable {
		return getConfiguredOrDefault(joinPoint, backEnd::getLong);
	}
	
	@Around("execution(@touchy.pad.RuntimeConfiguration float *..*.*())")
	public Float floatMethod(final ProceedingJoinPoint joinPoint) 
			throws Throwable {
		return getConfiguredOrDefault(joinPoint, backEnd::getFloat);
	}
	
	@Around("execution(@touchy.pad.RuntimeConfiguration double *..*.*())")
	public Double doubleMethod(final ProceedingJoinPoint joinPoint) 
			throws Throwable {
		return getConfiguredOrDefault(joinPoint, backEnd::getDouble);
	}
	
	@Around("execution(@touchy.pad.RuntimeConfiguration String *..*.*())")
	public String stringMethod(final ProceedingJoinPoint joinPoint) 
			throws Throwable {
		return getConfiguredOrDefault(joinPoint, backEnd::getString);
	}
}
