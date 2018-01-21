package touchy.pad;

import java.awt.Point;

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
	 * @param delta the movement to make
	 * @param click whether or not to click.
	 * @return the new position, so after moving and potentially clicking.
	 */
	Point move(Point delta, boolean left, boolean middle, boolean right);
	
	/**
	 * Lets the desktop scroll some amount of lines, returns the number of lines
	 * that we actually scrolled.
	 * @param amount the number of lines to scroll.
	 * @return the number of lines actually scrolled.
	 */
	void scroll(int amount);
	
	/**
	 * Types text on the PC. Can be used for typing or for sending textual 
	 * clipboard contents of the mobile device.
	 * @param text to type.
	 */
	void typeText(String text);
	
	/**
	 * @return the textual contents of the clipboard.
	 */
	String getClipboard();
	
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
	 * @author jan
	 *
	 */
	interface ServerProxy extends TouchLink, AutoCloseable {
		
	}
	
	/**
	 * Sink of this application. Real implementations should move the mouse etc.
	 * 
	 * @author Jan Groothuijse
	 */
	interface Backend extends TouchLink {
		
	}
}
