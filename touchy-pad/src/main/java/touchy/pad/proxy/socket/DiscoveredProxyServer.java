package touchy.pad.proxy.socket;

import java.net.InetAddress;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import touchy.pad.ProxyProvider;

/**
 * Stores information required to: 1) Allow the user to choose the right server
 * 2) Connect to the server the user choose.
 *
 * @author Jan Groothuijse
 */
@RequiredArgsConstructor
@Getter
class DiscoveredProxyServer implements ProxyProvider.DiscoveredServer {

    /**
     * Name of the server, it should send its name.
     */
    private final String serverName;
    /**
     * Address of the server, the client should be able to connect using this
     * information.
     */
    private final InetAddress address;

    @Override
    public String getName() {
        return serverName;
    }

    @Override
    public String getSpecification() {
        return address.toString();
    }
}
