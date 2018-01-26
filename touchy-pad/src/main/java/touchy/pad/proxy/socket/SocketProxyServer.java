package touchy.pad.proxy.socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
     * @param config the server, to obtain port number.
     * @param upstream the backend that actually moves things.
     * @throws IOException when the connection fails.
     */
    SocketProxyServer(final SocketProxyServerConfig config,
            final TouchLink.Backend upstream) throws IOException {
        backend = upstream;
        log.info("Creating server socket on port: " + config.getPort());
        serverSocket = new ServerSocket(config.getPort());
        log.info("Starting thread to accept connections.");
        new Thread(this).start(); // calls run in a new thread.
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            log.info("Listening on socket server.");
            // Keep listening untill an exception occurs.
            while (true) {
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
    private void handleConnection(final Socket socket) {
        log.info("handleConnection called");
        try (ObjectOutputStream output =
                new ObjectOutputStream(socket.getOutputStream())) {

            // Flush to unfreeze the ObjectInputStream on the other side, see
            // new ObjectInputStream().
            output.flush();

            try (ObjectInputStream input =
                    new ObjectInputStream(socket.getInputStream());) {
                // While the connection is open, we expect the client to send
                // a method proxy and wiat for the result to be returned.
                while (!socket.isClosed() && !serverSocket.isClosed()) {
                    log.info("Waiting for client input");
                    // Allow the client to send what needs to be done.
                    try {
                        final MethodProxy methodProxy;
                        methodProxy = (MethodProxy) input.readObject();
                        // Return the result of
                        final Object result = methodProxy.apply(backend);
                        output.writeObject(result);
                    } catch (EOFException e) {
                        log.error("Connection was closed.");
                    } catch (ClassNotFoundException e) {
                        log.error("Dropping message received from client"
                                + ", writing back a null value", e);
                        output.writeObject(null);
                    }
                }
                log.info("Socket is closed.");
            }

        } catch (IOException e1) {
            log.error("Network error, closing socket", e1);
            try {
                socket.close();
            } catch (IOException e2) {
                log.error("Failed to close the socket.");
            }
        }
    }

    @Override
    public void close() {
        // Closing the socket causes all blocked accept() method calls to return
        // with a socket exception.
        try {
            log.info("Closing server socket");
            serverSocket.close();
        } catch (IOException e) {
            log.error("Error closing server socket", e);
        }
    }
}
