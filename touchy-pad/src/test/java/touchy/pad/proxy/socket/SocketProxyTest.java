package touchy.pad.proxy.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.junit.Test;

import touchy.pad.TouchLink;

/**
 * Unit tests the socket proxy client en socket proxy server.
 * 
 * Testing both in isolation would require creating alternative implementation
 * of both, which is why they are unit-tested together here.
 * 
 * @author Jan Groothuijse
 */
public class SocketProxyTest {

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
            public void typeText(String text) {
                typed.set(true);
            }

            @Override
            public void scroll(int amount) {
                scrolled.set(true);
            }

            @Override
            public Supplier<Point> move(Point delta, boolean left,
                    boolean middle, boolean right) {
                return () -> delta;
            }

            @Override
            public Supplier<String> getClipboard() {
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

        try (final SocketProxyServer server =
                new SocketProxyServer(serverConfig, fakeBackend);
                final SocketProxyClient client =
                        new SocketProxyClient(clientConfig)) {

            // Test clipboard
            assertEquals(clipboard, client.getClipboard().get());

            // Test scrolling
            assertFalse(scrolled.get());
            client.scroll(0);
            assertTrue(scrolled.get());

            // Test typing
            assertFalse(typed.get());
            client.typeText("");
            assertTrue(typed.get());

            // Test moving
            final Point point = new Point(0, 0);
            assertEquals(point, client.move(point, true, true, true).get());
        }
    }
}
