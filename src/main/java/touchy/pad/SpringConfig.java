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
@ComponentScan({ "touchy.pad", "touchy.pad.aspects",
        "touchy.pad.desktopcontrol", "touchy.pad.proxy.socket",
        "touchy.pad.settings", "touchy.pad.web" })
@PropertySource("classpath:application.properties")
public class SpringConfig {

}
