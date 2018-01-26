package touchy.pad.backend;

import java.awt.AWTException;
import java.awt.Point;

/**
 * Test to visually see the AwtTouchLink at work.
 *
 * @author Jan Groothuijse
 */
interface ManualTest {
    /**
     * @param args will be ignored
     * @throws AWTException when run headless.
     */
    static void main(final String[] args) throws AWTException {
        final AwtTouchLink sut = new AwtTouchLink();
        final int distance = 100;
        sut.move(new Point(distance, distance), false, false, false);
    }
}
