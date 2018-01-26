package touchy.pad.backend;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import touchy.pad.TouchLink;

/**
 * TouchLink implementation using awt Robot.
 * 
 * Maintains some internal state to allow dragging and dropping.
 * 
 * @author Jan Groothuijse
 */
public class AwtTouchLink implements TouchLink {

    /**
     * State of the left mouse button.
     */
    final AtomicBoolean leftDown = new AtomicBoolean(false);
    /**
     * State of the right mouse button.
     */
    final AtomicBoolean rightDown = new AtomicBoolean(false);
    /**
     * State of the middle mouse button.
     */
    final AtomicBoolean middleDown = new AtomicBoolean(false);
    final Robot robot;

    /**
     * Constructor, because new Robot throws.
     * 
     * @throws AWTException when AWT is unavailabe, probably on a headless
     *             system.
     */
    public AwtTouchLink() throws AWTException {
        robot = new Robot();
    }

    @Override
    public Supplier<Point> move(final Point delta, final boolean left,
            final boolean middle, final boolean right) {
        // move first, click release later
        final Point pre = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(delta.x + pre.x, delta.y + pre.y);
        // handle clicks
        handleMousePress(leftDown, left, InputEvent.BUTTON1_DOWN_MASK);
        handleMousePress(middleDown, middle, InputEvent.BUTTON2_DOWN_MASK);
        handleMousePress(rightDown, right, InputEvent.BUTTON3_DOWN_MASK);

        return () -> MouseInfo.getPointerInfo().getLocation();
    }

    /**
     * Logic to handle mouse presses and releases.
     * 
     * @param state the current press/release status on the desktop
     * @param wanted the press/release status provided by move.
     * @param button the mouse button.
     */
    private void handleMousePress(AtomicBoolean state, boolean wanted,
            int button) {
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
                robot.mousePress(button);
            } else {
                robot.mouseRelease(button);
            }
        }
    }

    @Override
    public void scroll(final int amount) {
        robot.mouseWheel(amount);
    }

    @Override
    public void typeText(final String text) {

    }

    @Override
    public Supplier<String> getClipboard() {
        return () -> null;
    }

}
