package touchy.pad.proxy.socket;

import touchy.pad.RuntimeConfiguration;

/**
 * Client configuration.
 *
 * @author Jan Groothuijse
 */
public interface SocketProxyClientConfig {
    /**
     * @return hostname of the server as a string.
     */
    @RuntimeConfiguration
    default String getHost() {
        return "localhost";
    }

    /**
     * @return port number the server is listening on.
     */
    @RuntimeConfiguration
    default int getPort() {
        return SocketProxyServerConfig.DEFAULT_PORT;
    }
}
