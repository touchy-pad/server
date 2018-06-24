package touchy.pad.backend;

import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

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
public final class HeadFullAWTBackendTest {

    /**
     * System under test.
     */
    private final TouchLink sut;

    /**
     * Constructs a test.
     *
     * @throws IOException when reading IO fails
     * @throws UnsupportedFlavorException when the data flavor does not taste
     *             well.
     */
    public HeadFullAWTBackendTest()
            throws UnsupportedFlavorException, IOException {
        final Robot mockRobot = Mockito.mock(Robot.class);
        final Clipboard mockClipboard = Mockito.mock(Clipboard.class);
        final StringSelection selection = new StringSelection("");
        Mockito.when(mockClipboard.getContents(null)).thenReturn(selection);
        final PointerInfo mockPointerInfo = Mockito.mock(PointerInfo.class);
        Mockito.when(mockPointerInfo.getLocation()).thenReturn(new Point(0, 0));

        final DataFlavor dataFlavor = Mockito.mock(DataFlavor.class);
        Mockito.when(dataFlavor.getReaderForText(selection))
                .thenReturn(new DataFlavor(String.class, "text")
                        .getReaderForText(selection));

        final AwtSupplier awtSupplier;
        awtSupplier = new AwtSupplierImpl(mockRobot, mockClipboard,
                mockPointerInfo, dataFlavor);
        sut = new AwtTouchLink(awtSupplier);
    }

    /**
     * Check moving.
     */
    @Test
    public void checkMoving() {
        sut.move(new Point(0, 0), false, false, false).get();
        sut.move(new Point(0, 0), false, false, false).get();
        sut.move(new Point(0, 0), true, true, true).get();
        sut.move(new Point(0, 0), false, false, false).get();
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
        sut.receiveClipboard().get();
    }

    /**
     * Check receive clipboard.
     *
     * @throws IOException when reading IO fails
     * @throws UnsupportedFlavorException when the data flavor does not taste
     *             well.
     */
    @Test
    public void checkReceiveClipboardOnError()
            throws UnsupportedFlavorException, IOException {
        final TouchLink failingSut;
        final Robot mockRobot = Mockito.mock(Robot.class);
        final DataFlavor dataFlavor = Mockito.mock(DataFlavor.class);
        final StringSelection selection = new StringSelection("");
        final Clipboard mockClipboard = Mockito.mock(Clipboard.class);
        Mockito.when(mockClipboard.getContents(null)).thenReturn(selection);
        final PointerInfo mockPointerInfo = Mockito.mock(PointerInfo.class);
        Mockito.when(mockPointerInfo.getLocation()).thenReturn(new Point(0, 0));

        Mockito.when(dataFlavor.getReaderForText(selection))
                .thenThrow(new IOException());

        final AwtSupplier awtSupplier;
        awtSupplier = new AwtSupplierImpl(mockRobot, mockClipboard,
                mockPointerInfo, dataFlavor);
        failingSut = new AwtTouchLink(awtSupplier);
        failingSut.receiveClipboard().get();
    }
}
