package touchy.pad.proxy.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.junit.Test;
import org.springframework.util.StringUtils;

import touchy.pad.ProxyInitializationException;

/**
 * Tests the configuration classes of the proxy socket package, it checks that
 * the defaults are 'sane'.
 *
 * @author Jan Groothuijse
 */
public final class ProxySocketConfigTest {

    /**
     * Default implementation.
     */
    private final SocketProxyClientConfig clientConfig =
            new SocketClientProxyConfigImpl();

    /**
     * Checks the sanity of the default host name.
     */
    @Test
    public void checkHostNotEmpty() {
        assertTrue("Host name must not be empty.",
                !StringUtils.isEmpty(clientConfig.getHost()));
    }

    /**
     * Checks that the default port numbers are the same for client and server.
     */
    @Test
    public void checkPortsMatch() {

        final SocketProxyServerConfig serverConfig;
        serverConfig = new SocketProxyServerConfigImpl();
        assertEquals(
                "By default, the client and server portnumbers must be the "
                        + "same, to allow the program to work with no "
                        + "configuration.",
                serverConfig.getPort(), clientConfig.getPort());

    }

    /**
     * Checks that an exception gets thrown.
     *
     * @throws ProxyInitializationException always.
     * @throws UnknownHostException never.
     */
    @Test(expected = ProxyInitializationException.class)
    public void checkInsaneServerConfigLeadsToWrappedException()
            throws ProxyInitializationException, UnknownHostException {
        final SocketProxyServerConfig config = new SocketProxyServerConfig() {
            public int getPort() {
                return 1;
            }
        };

        final SocketProxyProvider provider;
        provider = new SocketProxyProvider(config, null, "0.0.0.0",
                "255.255.255.255");

        provider.getAndStartServer(null);
    }
}
