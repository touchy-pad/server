package touchy.pad.backend;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.junit.Test;
import org.mockito.Mockito;

import touchy.pad.TouchLink;
import touchy.pad.backend.AwtTouchLink.AwtSupplier;
import touchy.pad.backend.AwtTouchLink.AwtSupplierImpl;

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
    public HeadFullAWTBackendTest() {
        final Robot mockRobot = Mockito.mock(Robot.class);
        final Clipboard mockClipboard = Mockito.mock(Clipboard.class);
        Mockito.when(mockClipboard.getContents(null))
                .thenReturn(new StringSelection(""));
        final PointerInfo mockPointerInfo = Mockito.mock(PointerInfo.class);
        final AwtSupplier awtSupplier;
        awtSupplier =
                new AwtSupplierImpl(mockRobot, mockClipboard, mockPointerInfo);
        sut = new AwtTouchLink(awtSupplier);
    }

    /**
     * Check moving.
     */
    @Test
    public void checkMoving() {
        sut.move(new Point(0, 0), false, false, false).get();
        sut.move(new Point(0, 0), true, true, true).get();
    }

    /**
     * Check scrolling.
     */
    @Test
    public void checkScroll() {
        sut.scroll(0);
    }

    /**
     * Check send clipboard.
     */
    @Test
    public void checkSendClipboard() {
        sut.sendClipboard("");
    }

    /**
     * Check receive clipboard.
     */
    @Test
    public void checkReceiveClipboard() {
        sut.receiveClipboard();
    }
}
