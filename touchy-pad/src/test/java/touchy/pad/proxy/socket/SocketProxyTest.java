package touchy.pad.proxy.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.junit.Test;

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
public final class SocketProxyTest {

    /**
     * One big test-case to improve testing performance. Unit test should be
     * fast to execute.
     *
     * @throws Exception when something goes wrong.
     */
    @Test
    public void test() throws Exception {

        final String clipboard = "Clipboard contents";
        final int port = 9999;

        final AtomicBoolean scrolled = new AtomicBoolean(false);
        final AtomicBoolean typed = new AtomicBoolean(false);

        final TouchLink.Backend fakeBackend = new TouchLink.Backend() {

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
                return () -> clipboard;
            }
        };
        final SocketProxyServerConfig serverConfig =
                new SocketProxyServerConfig() {
                    @Override
                    public int getPort() {
                        return port;
                    }
                };
        final SocketProxyClientConfig clientConfig =
                new SocketProxyClientConfig() {

                    @Override
                    public String getHost() {
                        return "localhost";
                    }

                    @Override
                    public int getPort() {
                        return port;
                    }
                };

        final DiscoveredProxyServer discoveredProxy;
        discoveredProxy =
                new DiscoveredProxyServer("", InetAddress.getLocalHost());
        final SocketProxyProvider provider =
                new SocketProxyProvider(serverConfig, clientConfig);
        try (ServerProxy server = provider.getAndStartServer(fakeBackend);
                ClientProxy client = provider.getClient(discoveredProxy)) {

            // Test clipboard
            assertEquals(clipboard, client.receiveClipboard().get());

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

        final SocketProxyServer server =
                new SocketProxyServer(serverConfig, fakeBackend);
        final SocketProxyClient client =
                new SocketProxyClient(clientConfig, discoveredProxy);

        client.close();
        server.close();

    }
}
