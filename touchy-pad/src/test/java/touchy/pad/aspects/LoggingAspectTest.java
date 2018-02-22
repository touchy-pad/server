package touchy.pad.aspects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import touchy.pad.SpringConfig;
import touchy.pad.TouchLink;

/**
 * Tests logging aspects.
 * 
 * @author Jan Groothuijse
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
@ActiveProfiles({ "noTouchLink", "allwaysNull" })
public class LoggingAspectTest {

    /**
     * Touch link.
     */
    @Autowired
    private TouchLink touchLink;

    /**
     * Tests logging of touchlink events.
     */
    @Test
    public void testTouchLink() {
        touchLink.move(null, false, false, false);
        touchLink.scroll(0);
        touchLink.sendClipboard(null);
        touchLink.receiveClipboard();
        // TODO, assert loglines are written.
    }
}
