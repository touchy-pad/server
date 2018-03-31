package touchy.pad.proxy.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
     * @return the port used for discovery using broadcast.
     */
    @RuntimeConfiguration
    default String getServerName() {

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return SocketProxyServer.SERVER_NAME;
        }
    }
}
