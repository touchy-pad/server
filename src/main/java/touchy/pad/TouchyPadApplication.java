package touchy.pad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;

/**
 * Entry point for the application, uses Spring Boot to auto configure.
 *
 * @author Jan Groothuijse
 */
@SpringBootApplication
@ContextConfiguration(classes = SpringConfig.class)
public interface TouchyPadApplication {

    /**
     * @param args will be ignored for now.
     */
    static void main(final String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(TouchyPadApplication.class, args);
    }
}
