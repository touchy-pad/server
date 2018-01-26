package touchy.pad.proxy.socket;

import touchy.pad.RuntimeConfiguration;

/**
 * Configuration with default implementation to supply default values. Actual
 * configured value will be weaved in using aspects.
 *
 * @author Jan Groothuijse
 */
public interface SocketProxyServerConfig {

    /**
     * Default port number.
     */
    int DEFAULT_PORT = 9898;

    /**
     * @return the configured port number.
     */
    @RuntimeConfiguration
    default int getPort() {
        return DEFAULT_PORT;
    };
}
