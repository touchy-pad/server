package touchy.pad.connectivity.socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.extern.slf4j.Slf4j;
import touchy.pad.TouchLink;

/**
 * Implementation of serve proxy using sockets. Which means uses sockets to
 * connect to clients, those clients forward calls from the mobile device, and
 * the server proxy relais them to the backend.
 *
 * @author Jan Groothuijse
 */
@Slf4j
public final class SocketServer implements TouchLink.ServerProxy, Runnable {
    /**
     * In production this will hold the actual implementation mousing a pointer.
     */
    private final TouchLink.Backend backend;

    /**
     * Reference so run() and stop() have access.
     */
    private final ServerSocket serverSocket;

    /**
     * Ties lifecycle to to this objet.
     */
    private final DatagramSocket datagramSocket;

    /**
     * Configuration relevant to the server.
     */
    private final SocketServerConfig config;

    /**
     * Whether the server is still running.
     */
    private final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Thread to interrupt upon closing.
     */
    private final List<Thread> threads = new LinkedList<>();

    /**
     * To create socket related objects.
     */
    private final SocketUtils utils;

    /**
     * Name of the server.
     */
    public static final String SERVER_NAME = "Touchy pad server";

    /**
     * Default port number to listen on.
     */
    public static final int DEFAULT_PORT = 9898;

    /**
     * @param conf the server, to obtain port number.
     * @param upstream the backend that actually moves things.
     * @param address the address to bind to.
     * @param socketUtils to create sockets and address.
     * @throws IOException when the connection fails.
     */
    SocketServer(final SocketServerConfig conf,
            final TouchLink.Backend upstream, final InetAddress address,
            final SocketUtils socketUtils) throws IOException {
        backend = upstream;
        config = conf;
        // Listen on a port for connection.
        log.error("Creating server socket on port: " + config.getPort());
        serverSocket = new ServerSocket(config.getPort());
        log.error("Starting thread to accept connections.");
        final Thread listening = new Thread(this);
        listening.setName("listening on socket");
        addAndRun(listening);
        // Be discoverable through broadcast

        final int port = config.getDiscoveryPort();
        datagramSocket = socketUtils.datagramSocket(port, address);
        datagramSocket.setBroadcast(true);
        final Thread discoverability = new Thread(this::makeDiscoverable);
        discoverability.setName("disoverability");
        addAndRun(discoverability);
        utils = socketUtils;
    }

    /**
     * Makes this server discoverable through the udp socket.
     */
    void makeDiscoverable() {
        final int bufferSize = 15000;
        while (this.running.get()) {
            final byte[] buffer = new byte[bufferSize];
            final DatagramPacket packet;
            packet = new DatagramPacket(buffer, bufferSize);
            try {
                log.info("Discovery: Waiting for packet");
                datagramSocket.receive(packet);
                final String message = new String(packet.getData()).trim();
                final byte[] sendData =
                        config.getServerName().getBytes("UTF-8");
                if (message.equals(config.getDiscoveryRequest())) {
                    final DatagramPacket sendPacket;
                    sendPacket = new DatagramPacket(sendData, sendData.length,
                            packet.getAddress(), packet.getPort());
                    datagramSocket.send(sendPacket);
                    log.info("Discovery: send to "
                            + sendPacket.getAddress().getHostAddress());
                }
            } catch (IOException e) {
                log.info("Datagram socket closed, killing discovery thread");
                break;
            }
        }
    }

    @Override
    public void run() {
        final List<Socket> openedSockets = new LinkedList<>();
        try {
            log.info("Listening on socket server.");
            // Keep listening untill an exception occurs.
            while (running.get()) {
                // Blocking method, will stall the thread.
                final Socket socket = serverSocket.accept();
                openedSockets.add(socket);
                log.info("Socket opened.");
                // Create a new thread, just for the connection with this
                // client.
                final Thread connectionHandler;
                connectionHandler = new Thread(() -> handleConnection(socket));
                connectionHandler.setName("connectionHandler");
                addAndRun(connectionHandler);
            }
        } catch (IOException e) {
            log.info("Somebody closed to socket, killing thread that listened "
                    + "on it.");
        }
        openedSockets.forEach(openedSocket -> {
            try {
                openedSocket.close();
            } catch (IOException e) {
                log.error("Error while closing opened sockets", e);
            }
        });
    }

    /**
     * @param socket the connection socket.
     */
    void handleConnection(final Socket socket) {
        log.info("handleConnection called");
        try (ObjectOutputStream output =
                utils.objectOutputStream(socket.getOutputStream())) {

            // Flush to unfreeze the ObjectInputStream on the other side, see
            // new ObjectInputStream().
            output.flush();

            try (ObjectInputStream input =
                    utils.objectInputStream(socket.getInputStream());) {
                // While the connection is open, we expect the client to send
                // a method proxy and wait for the result to be returned.
                while (this.running.get()) {
                    log.info("Waiting for client input");
                    // Allow the client to send what needs to be done.
                    try { // NOSONAR
                        final MethodProxy methodProxy;
                        methodProxy = (MethodProxy) input.readObject();
                        // Return the result of
                        final Object result = methodProxy.apply(backend);
                        output.writeObject(result);
                    } catch (EOFException e) {
                        log.error("EOF, connection was closed.");
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e1) {
            log.error("Error communicating with client", e1);
        }
    }

    @Override
    public void close() throws IOException, InterruptedException {
        // Closing the socket causes all blocked accept() method calls to return
        // with a socket exception.
        log.info("Closing server socket");
        serverSocket.close();

        log.info("Closing discovery socket");
        datagramSocket.close();

        running.set(false);
        for (final Thread thread : threads) {
            log.info("Stopping {}", thread.getName());
            thread.join();
        }
    }

    /**
     * @param thread the thread to interrupt and join.
     */
    private void addAndRun(final Thread thread) {
        this.threads.add(thread);
        thread.start();
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Socket proxy server",
                "Listening on port: " + config.getPort(),
                "Link closed: " + serverSocket.isClosed());
    }
}
