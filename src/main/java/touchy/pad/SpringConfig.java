package touchy.pad;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration of this application.
 *
 * @author Jan Groothuijse
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan({ "touchy.pad.aspects", "touchy.pad.backend",
        "touchy.pad.proxy.socket" })
@PropertySource("classpath:application.properties")
public class SpringConfig {

}
