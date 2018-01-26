package touchy.pad;

/**
 * Exception when proxy fails to initialize.
 *
 * @author Jan Groothuijse
 */
public final class ProxyInitializationException extends Exception {

    /**
     * For serialization.
     */
    private static final long serialVersionUID = -4334505452787229888L;

    /**
     * @param e the wrapped exception.
     */
    public ProxyInitializationException(final Exception e) {
        super(e);
    }

}
