package touchy.pad.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * Aspect to add logging to the TouchLink interface, with the purpose of
 * standardizing logging across all implementations.
 * 
 * @author Jan Groothuijse
 */
@Slf4j
@Aspect
@ComponentScan
public class Logging {

	/**
	 * Selects all of TouchLinks methods and logs something with their names
	 * before invoking the method itself.
	 * 
	 * @param joinPoint to be injected.
	 */
	@Before("execution(* touchy.pad.TouchLink.*(..))")
	public void beforeMethodAdvice(final JoinPoint joinPoint) {
		
		// Log the name of the invoked method, to the logger defined by @Slf4j.
		log.info(joinPoint.getSignature().getName() + " called ");
	}
}
