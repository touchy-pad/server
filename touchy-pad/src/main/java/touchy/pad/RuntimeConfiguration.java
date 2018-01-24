package touchy.pad;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks an interface as providing access to runtime configuration.
 * 
 * This allows advice to be applied to all no-arg methods returning a primitive
 * type or a string.
 * 
 * @author Jan Groothuijse
 */
@Target(ElementType.METHOD)
public @interface RuntimeConfiguration {

}
