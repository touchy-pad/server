package touchy.pad.backend;

import static org.junit.Assert.assertNotEquals;

import java.awt.AWTException;
import java.awt.Point;

import org.junit.Test;

import touchy.pad.TouchLink;

/**
 * Test that needs a dekstop to run.
 * 
 * @author Jan Groothuijse
 */
public class HeadFullAWTBackendTest {

    /**
     * System under test.
     */
    private final TouchLink sut;

    /**
     * Constructs a test.
     * 
     * @throws AWTException when the test is run headless.
     */
    public HeadFullAWTBackendTest() throws AWTException {
        if (System.getProperty("java.awt.headless") == "false") {
            sut = new AwtTouchLink();
        } else {
            sut = null;
        }
    }

    /**
     * Check moving.
     */
    @Test
    public void checkMoving() {
        if (sut != null) {
            final int top = 100;
            final int left = 100;
            final Point init =
                    sut.move(new Point(0, 0), false, false, false).get();

            final Point toLeftUp;
            toLeftUp = sut.move(new Point(left, 0), false, false, false).get();

            final int movedLeft = toLeftUp.x - init.x;

            final Point back;
            back = sut.move(new Point(0, -movedLeft), false, false, false)
                    .get();

            final Point toRight;
            toRight = sut.move(new Point(-left, 0), false, false, false).get();

            final int movedRight = toRight.x - back.x;

            final Point back2;
            back2 = sut.move(new Point(0, -movedRight), false, false, false)
                    .get();

            // final int movedRight;

            assertNotEquals("Some movement should occur.", 0, movedLeft);
            assertNotEquals("Some movement should occur.", 0, movedRight);
        }
    }
}
