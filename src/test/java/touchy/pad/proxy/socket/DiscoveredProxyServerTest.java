package touchy.pad.proxy.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

/**
 * Tests DiscoveredProxyTest.
 *
 * @author Jan Groothuijse
 */
public final class DiscoveredProxyServerTest {
    /**
     * @throws UnknownHostException when localhost is not a known host.
     */
    @Test
    public void checkGetters() throws UnknownHostException {
        final String serverName = "serverName";
        final InetAddress address = InetAddress.getLocalHost();
        DiscoveredProxyServer server =
                new DiscoveredProxyServer(serverName, address);
        assertEquals(serverName, server.getName());
        assertEquals(address, server.getAddress());
        assertNotNull(server.getSpecification());
    }
}
