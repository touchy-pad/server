package touchy.pad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the application, uses Spring Boot to auto configure.
 * 
 * @author Jan Groothuijse
 */
@SpringBootApplication
public class TouchyPadApplication {

	/**
	 * @param args will be ignored for now.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TouchyPadApplication.class, args);
	}
}
