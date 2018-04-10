package touchy.pad.proxy.socket;

import java.awt.Point;

import org.junit.Test;

import touchy.pad.ProxyProvider.CloseableQueueProvider;
import touchy.pad.TouchLink.ClientProxy;
import touchy.pad.TouchLink.ServerProxy;
import touchy.pad.backend.NoTouchLink;

/**
 * Tests the discovery proxy.
 *
 * @author Jan Groothuijse
 */
public final class DiscoveryProxyTest {
    /**
     * Test that the server is discoverable, and that the discovered servers can
     * be connected to.
     *
     * @throws Exception when something bad happens.
     */
    @Test
    public void discovery() throws Exception {

        final SocketProxyServerConfig serverConfig;
        serverConfig = new SocketProxyServerConfig() {

        };
        final SocketProxyClientConfig clientConfig;
        clientConfig = new SocketProxyClientConfig() {

        };

        final SocketProxyProvider provider;
        provider = new SocketProxyProvider(serverConfig, clientConfig,
                "0.0.0.0", "255.255.255.255", new SocketUtilsImpl());
        System.out.println("Starting server using fake backend");
        final ServerProxy server =
                provider.getAndStartServer(new NoTouchLink());
        System.out.println("Getting queue");
        CloseableQueueProvider<DiscoveredProxyServer> discovered;
        discovered = provider.discoverServers();
        System.out.println("Received queue, waiting for discoved element.");
        DiscoveredProxyServer discoveredServer = discovered.get().take();
        System.out.println("Discovered a server");
        final ClientProxy client = provider.getClient(discoveredServer);
        System.out.println("Received client");
        client.sendClipboard("");
        client.move(new Point(0, 0), true, true, true);
        client.scroll(0);
        client.receiveClipboard();
        discovered.close();
        server.close();
        client.close();
    }
}
