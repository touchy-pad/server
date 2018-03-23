package touchy.pad.proxy.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
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

    @Override
    public ServerProxy getAndStartServer(final TouchLink.Backend backEnd)
            throws ProxyInitializationException {

        try {
            return new SocketProxyServer(serverConfig, backEnd);
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

    @Override
    public CloseableQueueProvider<DiscoveredProxyServer> discoverServers() {
        final BlockingQueue<DiscoveredProxyServer> list;
        list = new LinkedBlockingQueue<>();
        try {
            final DatagramSocket datagramSocket;
            datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            final byte[] sendData =
                    serverConfig.getDiscoveryRequest().getBytes();

            // Send dstuff
            final DatagramPacket sendPacket;
            // Try default 255.255.255.255 broadcast address.
            sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName("255.255.255.255"),
                    serverConfig.getDiscoveryPort());
            System.out.println("Client: Sending to 255.255.255.255.");
            datagramSocket.send(sendPacket);

            // Get broadcast addresses of all interfaces.
            for (NetworkInterface networkInterface : Collections
                    .list(NetworkInterface.getNetworkInterfaces())) {
                if (!networkInterface.isLoopback()) {
                    for (InterfaceAddress address : networkInterface
                            .getInterfaceAddresses()) {
                        final InetAddress broadcast = address.getBroadcast();
                        if (broadcast != null) {
                            final DatagramPacket sendAnother;
                            sendAnother = new DatagramPacket(sendData,
                                    sendData.length, broadcast,
                                    serverConfig.getDiscoveryPort());
                            System.out.println("Client: Sending to "
                                    + broadcast.getHostAddress());
                            datagramSocket.send(sendAnother);
                        }
                    }
                }
            }

            // Receive stuff
            final int bufferSize = 15000;
            final byte[] buffer = new byte[bufferSize];
            final DatagramPacket receivePacket;
            receivePacket = new DatagramPacket(buffer, buffer.length);
            Thread thread = new Thread(() -> {
                try {
                    // Quit at the first sign of trouble.
                    while (true) {
                        datagramSocket.receive(receivePacket);

                        final String message =
                                new String(receivePacket.getData()).trim();
                        datagramSocket.close();
                        list.add(new DiscoveredProxyServer(message,
                                receivePacket.getAddress()));
                        System.out.println(message + " from "
                                + receivePacket.getAddress().getHostAddress());
                    }
                } catch (SocketException e) {
                    log.info(
                            "DatagramSocket to broadcast for discovery closed");
                } catch (IOException e) {
                    System.out.println("Exception thrown in discovery thread.");
                    e.printStackTrace();

                }
            });
            thread.start();
            return new CloseableQueueProvider<DiscoveredProxyServer>() {

                @Override
                public BlockingQueue<DiscoveredProxyServer> get() {
                    return list;
                }

                @Override
                public void close() {
                    datagramSocket.close();
                }
            };
        } catch (Exception e) {
            return new CloseableQueueProvider<DiscoveredProxyServer>() {

                @Override
                public BlockingQueue<DiscoveredProxyServer> get() {
                    return null;
                }

                @Override
                public void close() throws IOException {

                }
            };
        }
    }
}
