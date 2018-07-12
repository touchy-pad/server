package touchy.pad.connectivity.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.junit.Test;
import org.springframework.util.StringUtils;

import touchy.pad.ConnectivityInitializationException;

/**
 * Tests the configuration classes of the proxy socket package, it checks that
 * the defaults are 'sane'.
 *
 * @author Jan Groothuijse
 */
public final class SocketConfigTest {

    /**
     * Default implementation.
     */
    private final SocketClientConfig clientConfig =
            new SocketClientConfigImpl();

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

        final SocketServerConfig serverConfig;
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
     * @throws ConnectivityInitializationException always.
     * @throws UnknownHostException never.
     */
    @Test(expected = ConnectivityInitializationException.class)
    public void checkInsaneServerConfigLeadsToWrappedException()
            throws ConnectivityInitializationException, UnknownHostException {
        final SocketServerConfig config = new SocketServerConfig() {
            public int getPort() {
                return 1;
            }

            @Override
            public String getMessage() {
                return null;
            }
        };

        final SocketConnectivityProvider provider;
        provider = new SocketConnectivityProvider(config, null, "0.0.0.0",
                "255.255.255.255", new SocketUtilsImpl());

        provider.getAndStartServer(null);
    }
}
