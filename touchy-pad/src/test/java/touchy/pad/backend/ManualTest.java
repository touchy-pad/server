package touchy.pad.backend;

import java.awt.AWTException;
import java.awt.Point;

/**
 * Test to visually see the AwtTouchLink at work.
 * 
 * @author Jan Groothuijse
 */
public class ManualTest {
    /**
     * @param args will be ignored
     * @throws AWTException
     */
    public static void main(String[] args) throws AWTException {
        final AwtTouchLink sut = new AwtTouchLink();
        sut.move(new Point(100, 100), false, false, false);
    }
}
