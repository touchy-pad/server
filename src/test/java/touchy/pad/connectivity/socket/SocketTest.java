package touchy.pad.connectivity.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;

import touchy.pad.ConnectivityProvider.CloseableQueueProvider;
import touchy.pad.TouchLink;
import touchy.pad.TouchLink.ClientProxy;
import touchy.pad.TouchLink.ServerProxy;

/**
 * Unit tests the socket proxy client en socket proxy server.
 *
 * Testing both in isolation would require creating alternative implementation
 * of both, which is why they are unit-tested together here.
 *
 * @author Jan Groothuijse
 */
public final class SocketTest {
    /**
     * Configuration.
     *
     * @param seed to add to the port number
     * @return a config for the client
     */
    private static SocketClientConfig clientConfig(final int seed) {
        return new SocketClientConfig() {
            @Override
            public String getHost() {
                return "localhost";
            }

            @Override
            public int getPort() {
                return PORT + seed;
            }

            @Override
            public String getMessage() {
                return null;
            }
        };
    }

    /**
     * Server config.
     *
     * @param seed to add to the port number
     * @return a config for the server
     */
    private static SocketServerConfig serverConfig(final int seed) {
        return new SocketServerConfig() {
            @Override
            public int getPort() {
                return PORT + seed;
            }

            @Override
            public String getMessage() {
                return null;
            }
        };
    }

    /**
     * Port number.
     */
    private static final int PORT = 19998;
    /**
     * Contents of the clipboard.
     */
    private static final String CLIPBOARD = "Clipboard contents";

    /**
     * If backend has scrolled.
     */
    private final AtomicBoolean scrolled = new AtomicBoolean(false);
    /**
     * If the backend has typed.
     */
    private final AtomicBoolean typed = new AtomicBoolean(false);

    /**
     * Mock backend implementation.
     */
    private final TouchLink.Backend fakeBackend = new TouchLink.Backend() {

        @Override
        public void sendClipboard(final String text) {
            typed.set(true);
        }

        @Override
        public void scroll(final int amount) {
            scrolled.set(true);
        }

        @Override
        public Supplier<Point> move(final Point delta, final boolean left,
                final boolean middle, final boolean right) {
            return () -> delta;
        }

        @Override
        public Supplier<String> receiveClipboard() {
            return () -> CLIPBOARD;
        }

        @Override
        public List<String> getDescription() {
            return Arrays.asList("Fake implementation");
        }
    };

    /**
     * Resets the state.
     */
    @Before
    public void resetState() {
        scrolled.set(false);
        typed.set(false);
    }

    /**
     * One big test-case to improve testing performance. Unit test should be
     * fast to execute.
     *
     * @throws Exception when something goes wrong.
     */
    @Test
    public void test() throws Exception {

        final DiscoveredSocketServer discoveredProxy;
        discoveredProxy =
                new DiscoveredSocketServer("", InetAddress.getLocalHost());
        final SocketConnectivityProvider provider =
                new SocketConnectivityProvider(serverConfig(0), clientConfig(0),
                        "0.0.0.0", "255.255.255.0", new SocketUtilsImpl());
        try (ServerProxy server = provider.getAndStartServer(fakeBackend);
                ClientProxy client = provider.getClient(discoveredProxy)) {

            // Test clipboard
            assertEquals(CLIPBOARD, client.receiveClipboard().get());

            // Test scrolling
            assertFalse(scrolled.get());
            client.scroll(0);
            assertTrue(scrolled.get());

            // Test typing
            assertFalse(typed.get());
            client.sendClipboard("");
            assertTrue(typed.get());

            // Test moving
            final Point point = new Point(0, 0);
            assertEquals(point, client.move(point, true, true, true).get());
        }

        final SocketServer server = new SocketServer(serverConfig(1),
                fakeBackend, InetAddress.getByName("0.0.0.0"),
                new SocketUtilsImpl());
        final SocketClient client = new SocketClient(clientConfig(1),
                discoveredProxy, new SocketUtilsImpl());

        client.close();
        server.close();
    }

    /**
     * Test that the server is discoverable, and that the discovered servers can
     * be connected to.
     *
     * @throws Exception when something fails.
     */
    @Test
    public void discovery() throws Exception {
        final SocketConnectivityProvider provider;
        provider =
                new SocketConnectivityProvider(serverConfig(2), clientConfig(2),
                        "0.0.0.0", "255.255.255.0", new SocketUtilsImpl());
        System.out.println("Starting server using fake backend");
        final ServerProxy server = provider.getAndStartServer(fakeBackend);
        System.out.println("Getting queue");
        CloseableQueueProvider<DiscoveredSocketServer> discovered;
        discovered = provider.discoverServers();
        System.out.println("Received queue, waiting for discoved element.");
        DiscoveredSocketServer discoveredSocketServer = discovered.get().take();
        System.out.println("Discovered a server");
        final ClientProxy client = provider.getClient(discoveredSocketServer);
        System.out.println("Received client");
        assertFalse(typed.get());
        client.sendClipboard("");
        assertTrue(typed.get());
        discovered.close();
        server.close();
        client.close();
    }
}
