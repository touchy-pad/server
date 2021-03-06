package touchy.pad.connectivity.socket;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.Mockito;

import lombok.extern.slf4j.Slf4j;
import touchy.pad.ConnectivityInitializationException;
import touchy.pad.ConnectivityProvider.CloseableQueueProvider;
import touchy.pad.TouchLink.ClientProxy;
import touchy.pad.TouchLink.ServerProxy;
import touchy.pad.desktopcontrol.NoTouchLink;

/**
 * Tests the discovery proxy.
 *
 * @author Jan Groothuijse
 */
@Slf4j
public final class SocketDiscoveryTest {

    /**
     * Server config.
     */
    private final SocketServerConfig serverConfig;
    /**
     * Client config.
     */
    private final SocketClientConfig clientConfig;

    /**
     * Default constructor.
     */
    public SocketDiscoveryTest() {
        serverConfig = new SocketServerConfig() {

            @Override
            public String getMessage() {
                return null;
            }

        };
        clientConfig = new SocketClientConfig() {

            @Override
            public String getMessage() {
                return null;
            }

        };
    }

    /**
     * Test that the server is discoverable, and that the discovered servers can
     * be connected to.
     *
     * @throws Exception when something bad happens.
     */
    @Test
    public void discovery() throws Exception {

        final SocketConnectivityProvider provider;
        provider = new SocketConnectivityProvider(serverConfig, clientConfig,
                "0.0.0.0", "255.255.255.255", new SocketUtilsImpl());

        final ServerProxy server =
                provider.getAndStartServer(new NoTouchLink());
        log.info("Getting queue");
        CloseableQueueProvider<DiscoveredSocketServer> discovered;
        discovered = provider.discoverServers();
        log.info("Received queue, waiting for discoved element.");
        DiscoveredSocketServer discoveredSocketServer = discovered.get().take();
        log.info("Discovered a server");
        final ClientProxy client = provider.getClient(discoveredSocketServer);
        log.info("Received client");
        client.sendClipboard("");
        client.move(new Point(0, 0), true, true, true);
        client.scroll(0);
        client.receiveClipboard();
        discovered.close();
        server.close();
        client.close();
    }

    /**
     * Check Socket exception handling in the run method of
     * RunnableClosableQueueProvider.
     *
     * @throws Exception when something bad happens.
     */
    @Test
    public void checkRunnableClosableQueueProviderRun() throws Exception {

        final DatagramSocket mockSocket = Mockito.mock(DatagramSocket.class);
        Mockito.doThrow(SocketException.class).when(mockSocket)
                .setBroadcast(true);
        final SocketConnectivityProvider provider;
        provider = new SocketConnectivityProvider(serverConfig, clientConfig,
                "0.0.0.0", "255.255.255.255", new SocketUtils() {
                    private final SocketUtilsImpl impl = new SocketUtilsImpl();

                    @Override
                    public ObjectOutputStream objectOutputStream(
                            final OutputStream os) throws IOException {
                        return impl.objectOutputStream(os);
                    }

                    @Override
                    public InetAddress addressByName(final String host)
                            throws UnknownHostException {
                        return impl.addressByName(host);
                    }

                    @Override
                    public DatagramSocket datagramSocket()
                            throws SocketException {
                        return mockSocket;
                    }

                    @Override
                    public DatagramSocket datagramSocket(final int port,
                            final InetAddress address) throws SocketException {
                        return impl.datagramSocket(port, address);
                    }

                    @Override
                    public ObjectInputStream objectInputStream(
                            final InputStream inputStream) throws IOException {
                        return impl.objectInputStream(inputStream);
                    }

                    @Override
                    public Socket socket(final int port,
                            final InetAddress address) throws IOException {
                        return impl.socket(port, address);
                    }
                });
        final NoTouchLink backEnd = new NoTouchLink();
        try (ServerProxy server = provider.getAndStartServer(backEnd)) {
            final CloseableQueueProvider<
                    DiscoveredSocketServer> discoverServers;
            discoverServers = provider.discoverServers();
            assertNotNull(discoverServers);
            final long timeOut = 0;
            discoverServers.get().poll(timeOut, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Check IO exception handling in the run method of
     * RunnableClosableQueueProvider.
     *
     * @throws Exception when something bad happens.
     */
    @Test
    public void checkRunnableClosableQueueProviderRunIException()
            throws Exception {

        final DatagramSocket mockSocket = Mockito.mock(DatagramSocket.class);
        Mockito.doThrow(IOException.class).when(mockSocket)
                .receive(Mockito.any(DatagramPacket.class));
        Mockito.when(mockSocket.isClosed()).thenReturn(false);
        final SocketConnectivityProvider provider;
        provider = new SocketConnectivityProvider(serverConfig, clientConfig,
                "0.0.0.0", "255.255.255.255", new SocketUtils() {
                    private final SocketUtilsImpl impl = new SocketUtilsImpl();

                    @Override
                    public ObjectOutputStream objectOutputStream(
                            final OutputStream os) throws IOException {
                        return impl.objectOutputStream(os);
                    }

                    @Override
                    public InetAddress addressByName(final String host)
                            throws UnknownHostException {
                        return impl.addressByName(host);
                    }

                    @Override
                    public DatagramSocket datagramSocket()
                            throws SocketException {
                        return mockSocket;
                    }

                    @Override
                    public DatagramSocket datagramSocket(final int port,
                            final InetAddress address) throws SocketException {
                        return impl.datagramSocket(port, address);
                    }

                    @Override
                    public ObjectInputStream objectInputStream(
                            final InputStream inputStream) throws IOException {
                        return impl.objectInputStream(inputStream);
                    }

                    @Override
                    public Socket socket(final int port,
                            final InetAddress address) throws IOException {
                        return impl.socket(port, address);
                    }
                });

        final NoTouchLink backEnd = new NoTouchLink();
        try (ServerProxy server = provider.getAndStartServer(backEnd)) {
            final CloseableQueueProvider<
                    DiscoveredSocketServer> discoverServers;
            discoverServers = provider.discoverServers();
            assertNotNull(discoverServers);
            final long timeOut = 0;
            discoverServers.get().poll(timeOut, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Checks exception handling in discoverServers.
     *
     * @throws Exception when something bad happens.
     */
    @Test
    public void checkDiscoverServersExceptionHandling() throws Exception {

        final SocketConnectivityProvider provider;
        provider = new SocketConnectivityProvider(serverConfig, clientConfig,
                "0.0.0.0", "255.255.255.255", new SocketUtils() {
                    private final SocketUtilsImpl impl = new SocketUtilsImpl();

                    @Override
                    public ObjectOutputStream objectOutputStream(
                            final OutputStream os) throws IOException {
                        return impl.objectOutputStream(os);
                    }

                    @Override
                    public InetAddress addressByName(final String host)
                            throws UnknownHostException {
                        return impl.addressByName(host);
                    }

                    @Override
                    public DatagramSocket datagramSocket()
                            throws SocketException {
                        throw new SocketException(
                                "Expected exception for testing");

                    }

                    @Override
                    public DatagramSocket datagramSocket(final int port,
                            final InetAddress address) throws SocketException {
                        return impl.datagramSocket(port, address);
                    }

                    @Override
                    public ObjectInputStream objectInputStream(
                            final InputStream inputStream) throws IOException {
                        return impl.objectInputStream(inputStream);
                    }

                    @Override
                    public Socket socket(final int port,
                            final InetAddress address) throws IOException {
                        return impl.socket(port, address);
                    }
                });
        final NoTouchLink backEnd = new NoTouchLink();
        try (ServerProxy server = provider.getAndStartServer(backEnd)) {
            assertNull(provider.discoverServers());
        }
    }

    /**
     * Checks exception handling in getClient.
     *
     * @throws Exception when something bad happens.
     */
    @Test(expected = ConnectivityInitializationException.class)
    public void checkGetClientExceptionHandling() throws Exception {

        final SocketConnectivityProvider provider;
        provider = new SocketConnectivityProvider(serverConfig, clientConfig,
                "0.0.0.0", "255.255.255.255", new SocketUtils() {
                    private final SocketUtilsImpl impl = new SocketUtilsImpl();

                    @Override
                    public ObjectOutputStream objectOutputStream(
                            final OutputStream os) throws IOException {
                        throw new IOException("Expected exception for testing");
                    }

                    @Override
                    public InetAddress addressByName(final String host)
                            throws UnknownHostException {
                        return impl.addressByName(host);
                    }

                    @Override
                    public DatagramSocket datagramSocket()
                            throws SocketException {
                        return impl.datagramSocket();
                    }

                    @Override
                    public DatagramSocket datagramSocket(final int port,
                            final InetAddress address) throws SocketException {
                        return impl.datagramSocket(port, address);
                    }

                    @Override
                    public ObjectInputStream objectInputStream(
                            final InputStream inputStream) throws IOException {
                        return impl.objectInputStream(inputStream);
                    }

                    @Override
                    public Socket socket(final int port,
                            final InetAddress address) throws IOException {
                        return impl.socket(port, address);
                    }
                });

        final NoTouchLink backEnd = new NoTouchLink();
        try (//
                ServerProxy server = provider.getAndStartServer(backEnd);
                CloseableQueueProvider<DiscoveredSocketServer> //
                discovered = provider.discoverServers()) {
            log.info("Received queue, waiting for discoved element.");
            DiscoveredSocketServer discoveredSocketServer =
                    discovered.get().take();
            log.info("Discovered a server");
            provider.getClient(discoveredSocketServer);
        }
    }
}
