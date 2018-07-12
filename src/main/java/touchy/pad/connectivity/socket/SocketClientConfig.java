package touchy.pad.connectivity.socket;

import touchy.pad.RuntimeConfiguration;
import touchy.pad.UserConfiguration;

/**
 * Client configuration.
 *
 * @author Jan Groothuijse
 */
public interface SocketClientConfig extends UserConfiguration {
    /**
     * @return hostname of the server as a string.
     */
    @RuntimeConfiguration("host")
    default String getHost() {
        return "localhost";
    }

    /**
     * @return port number the server is listening on.
     */
    @RuntimeConfiguration("port")
    default int getPort() {
        return SocketServer.DEFAULT_PORT;
    }
}
