package touchy.pad.proxy.socket;

import touchy.pad.RuntimeConfiguration;
import touchy.pad.UserConfiguration;

/**
 * Configuration with default implementation to supply default values. Actual
 * configured value will be weaved in using aspects.
 *
 * @author Jan Groothuijse
 */
public interface SocketProxyServerConfig extends UserConfiguration {

    /**
     * @return the configured port number.
     */
    @RuntimeConfiguration("port")
    default int getPort() {
        return SocketProxyServer.DEFAULT_PORT;
    }

    /**
     * @return the port used for discovery using broadcast.
     */
    @RuntimeConfiguration("discoveryPort")
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
    @RuntimeConfiguration("serverName")
    default String getServerName() {
        return SocketProxyServer.SERVER_NAME;
    }
}
