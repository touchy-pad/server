package touchy.pad;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test to see if the context will load.
 *
 * @author Jan Groothuijse
 *
 */
@RunWith(SpringRunner.class)
// To close the application context after the test:
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@ActiveProfiles({ "allwaysNull", "noTouchLink", "noProxyProvider" })
@SpringBootTest(classes = TouchyPadApplication.class)
public class TouchyPadApplicationTests {

    /**
     * Test to see if the application is configured correctly.
     */
    @Test
    public void contextLoads() {

    }
}
