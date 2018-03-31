package touchy.pad.proxy.socket;

import org.junit.Test;

import touchy.pad.ProxyProvider.CloseableQueueProvider;
import touchy.pad.TouchLink.ClientProxy;
import touchy.pad.TouchLink.ServerProxy;

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

        final SocketProxyProvider provider;
        provider = new SocketProxyProvider(new SocketProxyServerConfig() {
        }, new SocketProxyClientConfig() {
        }, "0.0.0.0", "255.255.255.255");
        System.out.println("Starting server using fake backend");
        final ServerProxy server = provider.getAndStartServer(null);
        System.out.println("Getting queue");
        CloseableQueueProvider<DiscoveredProxyServer> discovered;
        discovered = provider.discoverServers();
        System.out.println("Received queue, waiting for discoved element.");
        DiscoveredProxyServer discoveredServer = discovered.get().take();
        System.out.println("Discovered a server");
        final ClientProxy client = provider.getClient(discoveredServer);
        System.out.println("Received client");
        client.sendClipboard("");
        discovered.close();
        server.close();
        client.close();
    }
}
