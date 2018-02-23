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
     * Default port number to listen on.
     */
    int DEFAULT_PORT = 9898;

    /**
     * Default port number for discovery queries.
     */
    int DISCOVERY_PORT = 8989;

    /**
     * Default payload for discovery.
     */
    String DISCOVERY_REQUEST = "DISCOVERY_REQUEST";
    /**
     * Name of the server.
     */
    String SERVER_NAME = "Touchy pad server";

    /**
     * @return the configured port number.
     */
    @RuntimeConfiguration
    default int getPort() {
        return DEFAULT_PORT;
    }

    /**
     * @return the port used for discovery using broadcast.
     */
    @RuntimeConfiguration
    default int getDiscoveryPort() {
        return DISCOVERY_PORT;
    }

    /**
     * @return request to send when discovering servers.
     */
    default String getDiscoveryRequest() {
        return DISCOVERY_REQUEST;
    }

    /**
     * @return the port used for discovery using broadcast.
     */
    @RuntimeConfiguration
    default String getServerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return SERVER_NAME;
        }
    }
}
