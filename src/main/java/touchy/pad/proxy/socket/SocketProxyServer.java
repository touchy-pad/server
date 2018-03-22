package touchy.pad.proxy.socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.experimental.Delegate;
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
public final class SocketProxyServer
        implements TouchLink.ServerProxy, Runnable {
    /**
     * In production this will hold the actual implementation mousing a pointer.
     */
    @Delegate
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
    private final SocketProxyServerConfig config;

    /**
     * Whether the server is still running.
     */
    private final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Thread to interrupt upon closing.
     */
    private final List<Thread> threads = new LinkedList<>();

    /**
     * @param conf the server, to obtain port number.
     * @param upstream the backend that actually moves things.
     * @throws IOException when the connection fails.
     */
    SocketProxyServer(final SocketProxyServerConfig conf,
            final TouchLink.Backend upstream) throws IOException {
        backend = upstream;
        this.config = conf;
        // Listen on a port for connection.
        log.error("Creating server socket on port: " + config.getPort());
        serverSocket = new ServerSocket(config.getPort());
        log.error("Starting thread to accept connections.");
        new Thread(this).start(); // calls run in a new thread.

        // Be discoverable through broadcast
        final InetAddress address;
        address = InetAddress.getByName("0.0.0.0");
        datagramSocket = new DatagramSocket(config.getDiscoveryPort(), address);
        datagramSocket.setBroadcast(true);
        new Thread(this::makeDiscoverable).start();
    }

    /**
     * Makes this server discoverable through the udp socket.
     */
    void makeDiscoverable() {
        final int bufferSize = 15000;
        while (true) {
            final byte[] buffer = new byte[bufferSize];
            final DatagramPacket packet;
            packet = new DatagramPacket(buffer, bufferSize);
            try {
                System.out.println("Server: Waiting for packet");
                datagramSocket.receive(packet);
                final String message = new String(packet.getData()).trim();
                final byte[] sendData =
                        config.getServerName().getBytes("UTF-8");
                if (message.equals(config.getDiscoveryRequest())) {
                    final DatagramPacket sendPacket;
                    sendPacket = new DatagramPacket(sendData, sendData.length,
                            packet.getAddress(), packet.getPort());
                    datagramSocket.send(sendPacket);
                    System.out.println("Server: send to "
                            + sendPacket.getAddress().getHostAddress());
                }
            } catch (IOException e) {
                // socket closed.
                break;
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            log.info("Listening on socket server.");
            // Keep listening untill an exception occurs.
            while (running.get()) {
                // Blocking method, will stall the thread.
                final Socket socket = serverSocket.accept();
                log.info("Socket opened.");
                // Create a new thread, just for the connection with this
                // client.
                new Thread(() -> handleConnection(socket)).start();
            }
        } catch (IOException e) {
            log.info("Somebody closed to socket, killing thread that listened "
                    + "on it.");
        }
    }

    /**
     * @param socket the connection socket.
     */
    void handleConnection(final Socket socket) {
        log.info("handleConnection called");
        try (ObjectOutputStream output =
                new ObjectOutputStream(socket.getOutputStream())) {

            // Flush to unfreeze the ObjectInputStream on the other side, see
            // new ObjectInputStream().
            output.flush();

            try (ObjectInputStream input =
                    new ObjectInputStream(socket.getInputStream());) {
                // While the connection is open, we expect the client to send
                // a method proxy and wait for the result to be returned.
                while (!socket.isClosed() && this.running.get()) {
                    log.info("Waiting for client input");
                    // Allow the client to send what needs to be done.
                    try {
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
                log.info("Socket is closed.");
            }

        } catch (IOException | ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        // Closing the socket causes all blocked accept() method calls to return
        // with a socket exception.
        log.info("Closing server socket");
        serverSocket.close();

        log.info("Closing discovery socket");
        datagramSocket.close();

        running.set(false);
        threads.forEach(Thread::interrupt);
        threads.forEach(arg0 -> {
            try {
                arg0.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
}
