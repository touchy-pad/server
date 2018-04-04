package touchy.pad.proxy.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import touchy.pad.ProxyInitializationException;
import touchy.pad.ProxyProvider;
import touchy.pad.TouchLink;
import touchy.pad.TouchLink.ClientProxy;
import touchy.pad.TouchLink.ServerProxy;

/**
 * Provides proxy using the Socket Proxy implementations.
 *
 * While this is a singleton, both the mobile device and the server running on
 * the pc will have a different version.
 *
 * @author Jan Groothuijse
 */
@Component("socketProxy")
@Slf4j
public final class SocketProxyProvider
        implements ProxyProvider<DiscoveredProxyServer> {

    /**
     * Server config.
     */
    private final SocketProxyServerConfig serverConfig;

    /**
     * Client config.
     */
    private final SocketProxyClientConfig clientConfig;
    /**
     * Address to refer to all addresses.
     */
    private final InetAddress address;
    /**
     * Broadcast address.
     */
    private final InetAddress broadcastAddress;

    /**
     * @param serverCfg the runtime server config
     * @param clientCfg the runtime client config
     * @param anyIp ip address of any host as a string (0.0.0.0 or ::).
     * @param broadcastIp the ip to broadcast (255.255.255.255 on ipv4).
     * @throws UnknownHostException the host is not known.
     */
    SocketProxyProvider(//
            final SocketProxyServerConfig serverCfg,
            final SocketProxyClientConfig clientCfg,
            final @Value("${touchy.anyIp}") String anyIp,
            final @Value("${touchy.broadcastIp}") String broadcastIp)
            throws UnknownHostException {
        address = InetAddress.getByName(anyIp);
        broadcastAddress = InetAddress.getByName(broadcastIp);
        serverConfig = serverCfg;
        clientConfig = clientCfg;

    }

    @Override
    public ServerProxy getAndStartServer(final TouchLink.Backend backEnd)
            throws ProxyInitializationException {

        try {
            return new SocketProxyServer(serverConfig, backEnd, address);
        } catch (IOException e) {
            log.error("IOException while starting server on port: "
                    + serverConfig.getPort(), e);
            // Handle these errors in the interface, so that the user may
            // change config and/or retry.
            throw new ProxyInitializationException(e);
        }
    }

    @Override
    public ClientProxy getClient(final DiscoveredProxyServer connectTo)
            throws ProxyInitializationException {
        try {
            return new SocketProxyClient(clientConfig, connectTo);
        } catch (IOException e) {
            // Handle these errors in the interface, so that the user may
            // change choose another server and/or retry.
            throw new ProxyInitializationException(e);
        }
    }

    /**
     * Combines a list and socket, ties the lifetime of the socket to the
     * lifetime of the list provider.
     */
    private class RunnableClosableQueueProvider
            implements CloseableQueueProvider<DiscoveredProxyServer>, Runnable {
        /**
         * Socket to send and receive data.
         */
        private final DatagramSocket datagramSocket;
        /**
         * List of discovered servers.
         */
        private final BlockingQueue<DiscoveredProxyServer> list;

        /**
         * @throws SocketException when no new datagram socket can be made.
         */
        RunnableClosableQueueProvider() throws SocketException {
            list = new LinkedBlockingQueue<>();
            datagramSocket = new DatagramSocket();
        }

        @Override
        public BlockingQueue<DiscoveredProxyServer> get() {
            return list;
        }

        @Override
        public void close() throws IOException {
            datagramSocket.close();

        }

        @Override
        public void run() {
            try {
                datagramSocket.setBroadcast(true);
                final byte[] sendData;
                sendData = serverConfig.getDiscoveryRequest().getBytes();

                // Get broadcast addresses of all interfaces.
                sendBroadcast(sendData);

                // Receive stuff
                final int bufferSize = 15000;
                final byte[] buffer = new byte[bufferSize];
                final DatagramPacket receivePacket;
                receivePacket = new DatagramPacket(buffer, buffer.length);

                // Quit at the first sign of trouble.
                while (!datagramSocket.isClosed()) {
                    datagramSocket.receive(receivePacket);

                    final String message;
                    message = new String(receivePacket.getData()).trim();
                    datagramSocket.close();
                    list.add(new DiscoveredProxyServer(message,
                            receivePacket.getAddress()));
                    log.info(message + " from "
                            + receivePacket.getAddress().getHostAddress());
                }
            } catch (SocketException e) {
                log.info("DatagramSocket to broadcast for discovery closed");
            } catch (IOException e) {
                log.info("Exception thrown in discovery thread.", e);
            }
        }

        /**
         * Sends sendData to the entire network.
         *
         * @param sendData the data to broadcast.
         * @throws IOException when the network fails.
         */
        private void sendBroadcast(final byte[] sendData) throws IOException {
            // Send stuff
            final DatagramPacket sendPacket;
            // Try default 255.255.255.255 broadcast address.
            sendPacket = new DatagramPacket(sendData, sendData.length,
                    broadcastAddress, serverConfig.getDiscoveryPort());
            log.info("Sending to {}.", broadcastAddress.getHostAddress());
            datagramSocket.send(sendPacket);

            for (NetworkInterface networkInterface : Collections
                    .list(NetworkInterface.getNetworkInterfaces())) {
                if (!networkInterface.isLoopback()) {
                    for (InterfaceAddress hostAddress : networkInterface
                            .getInterfaceAddresses()) {
                        final InetAddress broadcast =
                                hostAddress.getBroadcast();
                        if (broadcast != null) {
                            final DatagramPacket sendAnother;
                            sendAnother = new DatagramPacket(sendData,
                                    sendData.length, broadcast,
                                    serverConfig.getDiscoveryPort());
                            log.info(
                                    "Sending to " + broadcast.getHostAddress());
                            datagramSocket.send(sendAnother);
                        }
                    }
                }
            }
        }
    }

    @Override
    public CloseableQueueProvider<DiscoveredProxyServer> discoverServers() {
        try {
            final RunnableClosableQueueProvider result;
            result = new RunnableClosableQueueProvider();
            new Thread(result).start();
            return result;
        } catch (SocketException e) {
            return null;
        }
    }
}
