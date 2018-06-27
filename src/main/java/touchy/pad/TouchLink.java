package touchy.pad;

import java.awt.Point;
import java.util.List;
import java.util.function.Supplier;

/**
 * Interface to the object moving the pointer on the screen of the PC.
 *
 * The actual implentation will reside on the PC, on the mobile device a proxy
 * will be available to forward all calls.
 *
 * @author Jan Groothuijse
 *
 */
public interface TouchLink {
    /**
     * Moves the pointer by the delta amount, and clicks if click is true.
     *
     * @param delta the movement to make
     * @param left whether or not left is clicking.
     * @param middle whether or not middle is clicking.
     * @param right whether or not right is clicking.
     * @return the new position, so after moving and potentially clicking.
     */
    Supplier<Point> move(Point delta, boolean left, boolean middle,
            boolean right);

    /**
     * Lets the desktop scroll some amount of lines, returns the number of lines
     * that we actually scrolled.
     *
     * @param amount the number of lines to scroll.
     */
    void scroll(int amount);

    /**
     * Sends a string to the clipboard of the server.
     *
     * @param text to type.
     */
    void sendClipboard(String text);

    /**
     * @return the textual contents of the clipboard.
     */
    Supplier<String> receiveClipboard();

    /**
     * @return list of textual descriptions.
     */
    List<String> getDescription();

    /**
     * Flavor of TouchLink to indicate that the implementation live on the
     * client as proxy for the real implementation.
     *
     * @author Jan Groothuijse
     *
     */
    interface ClientProxy extends TouchLink, AutoCloseable {

    }

    /**
     * Flavor of TouchLink to connect a network proxied client to the backend.
     *
     * @author Jan Groothuijse
     *
     */
    interface ServerProxy extends AutoCloseable {

        /**
         * @return list of textual descriptions.
         */
        List<String> getDescription();
    }

    /**
     * Sink of this application. Real implementations should move the mouse etc.
     *
     * @author Jan Groothuijse
     */
    interface Backend extends TouchLink {

    }
}
