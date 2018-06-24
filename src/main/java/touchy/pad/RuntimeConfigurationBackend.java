package touchy.pad;

/**
 * Interface to get configuration values based on name and type.
 *
 * @author Jan Groothuijse
 */
public interface RuntimeConfigurationBackend {
    /**
     * @param name the name of the configuration directive.
     * @return the configured value or null.
     */
    Boolean getBoolean(String name);

    /**
     * @param name the name of the configuration directive.
     * @param value the configured value.
     */
    void setBoolean(String name, boolean value);

    /**
     * @param name the name of the configuration directive.
     * @return the configured value or null.
     */
    Integer getInteger(String name);

    /**
     * @param name the name of the configuration directive.
     * @param value the configured value.
     */
    void setInt(String name, int value);

    /**
     * @param name the name of the configuration directive.
     * @return the configured value or null.
     */
    Long getLong(String name);

    /**
     * @param name the name of the configuration directive.
     * @param value the configured value.
     */
    void setLong(String name, long value);

    /**
     * @param name the name of the configuration directive.
     * @return the configured value or null.
     */
    Float getFloat(String name);

    /**
     * @param name the name of the configuration directive.
     * @param value the configured value.
     */
    void setFloat(String name, float value);

    /**
     * @param name the name of the configuration directive.
     * @return the configured value or null.
     */
    Double getDouble(String name);

    /**
     * @param name the name of the configuration directive.
     * @param value the configured value.
     */
    void setDouble(String name, double value);

    /**
     * @param name the name of the configuration directive.
     * @return the configured value or null.
     */
    String getString(String name);

    /**
     * @param name the name of the configuration directive.
     * @param value the configured value.
     */
    void setString(String name, String value);
}
