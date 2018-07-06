package touchy.pad;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Initialized servlet to deploy the dispatcherServlet. Picked up by the
 * servelet container, hooks in Spring.
 *
 * @author Jan Groothuijse *
 */
public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected final SpringApplicationBuilder
            configure(final SpringApplicationBuilder application) {
        return application.sources(TouchyPadApplication.class);
    }
}
