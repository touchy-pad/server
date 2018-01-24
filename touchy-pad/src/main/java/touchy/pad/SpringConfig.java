package touchy.pad;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration of this application.
 * 
 * @author Jan Groothuijse
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan({ "touchy.pad.aspects", "touchy.pad.backend",
        "touchy.pad.proxy.socket" })
public class SpringConfig {

}
