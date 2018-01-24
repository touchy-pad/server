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
	 * @return the configured value or null.
	 */
	Byte getByte(String name);
	/**
	 * @param name the name of the configuration directive.
	 * @return the configured value or null.
	 */
	Character getChar(String name);
	/**
	 * @param name the name of the configuration directive.
	 * @return the configured value or null.
	 */
	Short getShort(String name);
	/**
	 * @param name the name of the configuration directive.
	 * @return the configured value or null.
	 */
	Integer getInteger(String name);
	/**
	 * @param name the name of the configuration directive.
	 * @return the configured value or null.
	 */
	Long getLong(String name);
	/**
	 * @param name the name of the configuration directive.
	 * @return the configured value or null.
	 */
	Float getFloat(String name);
	/**
	 * @param name the name of the configuration directive.
	 * @return the configured value or null.
	 */
	Double getDouble(String name);
	/**
	 * @param name the name of the configuration directive.
	 * @return the configured value or null.
	 */
	String getString(String name);
}
