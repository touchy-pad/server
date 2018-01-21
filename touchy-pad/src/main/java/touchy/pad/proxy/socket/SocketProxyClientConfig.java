package touchy.pad.proxy.socket;

/**
 * Client configuration.
 * 
 * @author Jan Groothuijse
 *
 */
public interface SocketProxyClientConfig {
	/**
	 * @return hostname of the server as a string.
	 */
	String getHost();
	/**
	 * @return port number the server is listening on.
	 */
	int getPort();
}
