package touchy.pad.backend;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import touchy.pad.TouchLink;

/**
 * TouchLink implementation using awt Robot.
 *
 * Maintains some internal state to allow dragging and dropping.
 *
 * @author Jan Groothuijse
 */
@Slf4j
public final class AwtTouchLink implements TouchLink {

    /**
     * State of the left mouse button.
     */
    private final AtomicBoolean leftDown = new AtomicBoolean(false);
    /**
     * State of the right mouse button.
     */
    private final AtomicBoolean rightDown = new AtomicBoolean(false);
    /**
     * State of the middle mouse button.
     */
    private final AtomicBoolean middleDown = new AtomicBoolean(false);
    /**
     * Upstream, the thing that moves the pointer etc.
     */
    private final AwtSupplier awtSupplier;

    /**
     * @param r awtSupplier to use.
     */
    public AwtTouchLink(final AwtSupplier r) {
        awtSupplier = r;
    }

    @Override
    public Supplier<Point> move(final Point delta, final boolean left,
            final boolean middle, final boolean right) {
        // move first, click release later
        final Point pre;
        pre = MouseInfo.getPointerInfo().getLocation();
        awtSupplier.getRobot().mouseMove(delta.x + pre.x, delta.y + pre.y);
        // handle clicks
        handleMousePress(leftDown, left, InputEvent.BUTTON1_DOWN_MASK);
        handleMousePress(middleDown, middle, InputEvent.BUTTON2_DOWN_MASK);
        handleMousePress(rightDown, right, InputEvent.BUTTON3_DOWN_MASK);

        return () -> awtSupplier.getPointerInfo().getLocation();
    }

    /**
     * Logic to handle mouse presses and releases.
     *
     * @param state the current press/release status on the desktop
     * @param wanted the press/release status provided by move.
     * @param button the mouse button.
     */
    private void handleMousePress(final AtomicBoolean state,
            final boolean wanted, final int button) {
        // If state and wanted are not in sync, then we must press or release
        // the mouse button. First we check if we need to set, then we change
        // the
        // the value of state a using CompareAndSwap, meaning if it was updated
        // in the mean time we abort.
        // When we do not abort, we press or release depending on wanted.
        if (state.get() != wanted && state.compareAndSet(!wanted, wanted)) {
            // At this point, state has been changed so we are committed.
            // Now that we now a change is needed, we query wanted to see where
            // we are going.
            if (wanted) {
                awtSupplier.getRobot().mousePress(button);
            } else {
                awtSupplier.getRobot().mouseRelease(button);
            }
        }
    }

    @Override
    public void scroll(final int amount) {
        awtSupplier.getRobot().mouseWheel(amount);
    }

    @Override
    public void sendClipboard(final String text) {
        awtSupplier.getClipboard().setContents(new StringSelection(text), null);
    }

    @Override
    public Supplier<String> receiveClipboard() {
        try {
            final String content;
            final Transferable transferable;
            final DataFlavor flavor = new DataFlavor(String.class, "text");
            transferable = awtSupplier.getClipboard().getContents(null);
            content = IOUtils.toString(flavor.getReaderForText(transferable));
            return () -> content;
        } catch (IOException | UnsupportedFlavorException e) {
            log.error("Exception while getting clipboard contents", e);
            return () -> "";
        }

    }

    /**
     * Decouples AWT dependencies, so they can be mocked.
     *
     * @author Jan Groothuijse
     */
    interface AwtSupplier {
        /**
         * @return Robot instance to move cursor.
         */
        Robot getRobot();

        /**
         * @return Clipboard instance to manipulate.
         */
        Clipboard getClipboard();

        /**
         * @return to query the mouse position.
         */
        PointerInfo getPointerInfo();
    }

    /**
     * Default, production implementation. Needs to run on a non-headless
     * system.
     *
     * @author Jan Groothuijse
     */
    @Component
    @AllArgsConstructor
    @Profile("!noTouchLink")
    @Getter
    public static class AwtSupplierImpl implements AwtSupplier {

        /**
         * Awt robot instance.
         */
        private final Robot robot;
        /**
         * Clipboard of the system.
         */
        private final Clipboard clipboard;
        /**
         * To query the mouse position.
         */
        private final PointerInfo pointerInfo;

        /**
         * Construct an awt supplier based, if ran on a system with a display.
         *
         * @throws HeadlessException when the system has no display.
         * @throws AWTException when awt is unhappy.
         */
        public AwtSupplierImpl() throws HeadlessException, AWTException {
            this(new Robot(), Toolkit.getDefaultToolkit().getSystemClipboard(),
                    MouseInfo.getPointerInfo());
        }
    }
}
