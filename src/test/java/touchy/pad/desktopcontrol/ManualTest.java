package touchy.pad.desktopcontrol;

import java.awt.AWTException;
import java.awt.Point;

import touchy.pad.desktopcontrol.AwtTouchLink;

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
        final AwtTouchLink sut =
                new AwtTouchLink(new AwtTouchLink.AwtSupplierImpl());
        final int distance = 100;
        sut.move(new Point(distance, distance), false, false, false);

        // sut.typeText("fooo");
    }
}
