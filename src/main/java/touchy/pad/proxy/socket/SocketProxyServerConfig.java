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
     * @return the configured port number.
     */
    @RuntimeConfiguration
    default int getPort() {
        return SocketProxyServer.DEFAULT_PORT;
    }

    /**
     * @return the port used for discovery using broadcast.
     */
    @RuntimeConfiguration
    default int getDiscoveryPort() {
        return DiscoveredProxyServer.DISCOVERY_PORT;
    }

    /**
     * @return request to send when discovering servers.
     */
    default String getDiscoveryRequest() {
        return DiscoveredProxyServer.DISCOVERY_REQUEST;
    }

    /**
     * @return the name of the server.
     */
    @RuntimeConfiguration
    default String getServerName() {
        return SocketProxyServer.SERVER_NAME;
    }
}
