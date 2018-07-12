package touchy.pad;

/**
 * Marks a class as user configurable so it can be picked up by reflection.
 *
 * @author Jan Groothijse
 */
public interface UserConfiguration {
    /**
     * @return message referring to localized name and descriptions for the
     *         class or configuration. The message will be prefixed with config.
     */
    String getMessage();
}
