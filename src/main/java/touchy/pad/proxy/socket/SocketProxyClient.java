package touchy.pad.proxy.socket;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import touchy.pad.TouchLink;

/**
 * Proxy for the TouchLink.Backend, lives on the mobile device. Calls are
 * forwarded through the SocketProxyServer.
 *
 * @author Jan Groothuijse
 */
@Slf4j
public final class SocketProxyClient
        implements TouchLink.ClientProxy, AutoCloseable {

    /**
     * Reference to close.
     */
    private final Socket client;

    /**
     * Reference to close.
     */
    private final ObjectOutputStream output;

    /**
     * Reference to close.
     */
    private final ObjectInputStream input;

    /**
     * @param config to get port number and hostname.
     * @param connectTo connect to a server.
     * @param socketUtils to get socket and object io streams.
     * @throws IOException when the connection fails.
     */
    public SocketProxyClient(final SocketProxyClientConfig config,
            final DiscoveredProxyServer connectTo,
            final SocketUtils socketUtils) throws IOException {

        log.info("Creating client connection.");
        client = socketUtils.socket(config.getPort(), connectTo.getAddress());
        output = socketUtils.objectOutputStream(client.getOutputStream());
        log.info("Created client output.");
        // Flush to unfreeze the ObjectInputStream on the other side, see new
        // ObjectInputStream().
        output.flush();
        log.info("Flushed.");
        input = socketUtils.objectInputStream(client.getInputStream());
    }

    /**
     * Send and receive to the other side.
     *
     * @param proxy the method proxy.
     * @return what the message proxy returned when it executed on the other
     *         side.
     */
    private synchronized Object sendAndReceive(final MethodProxy proxy) {
        try {
            output.writeObject(proxy);
        } catch (IOException e) {
            log.error("Network error while writing, proceeding to read", e);
        }
        try {
            return input.readObject();
        } catch (IOException e) {
            log.error("Network error while reading", e);
        } catch (ClassNotFoundException e) {
            log.error("Unknown class while reading response from " + "server",
                    e);
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        try {
            output.close();
        } catch (IOException e) {
            // Do nothing, nothing can be done.
        }
        input.close();
        client.close();
    }

    @Override
    public Supplier<Point> move(final Point delta, final boolean left,
            final boolean middle, final boolean right) {

        final MethodProxy.Move move;
        move = new MethodProxy.Move(delta, right, right, right);
        final Point point = (Point) sendAndReceive(move);
        return () -> point;
    }

    @Override
    public void scroll(final int amount) {
        sendAndReceive(new MethodProxy.Scroll(amount));
    }

    @Override
    public void sendClipboard(final String text) {
        sendAndReceive(new MethodProxy.TypeText(text));
    }

    @Override
    public Supplier<String> receiveClipboard() {
        final String string;
        string = (String) sendAndReceive(new MethodProxy.GetClipboard());
        return () -> string;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Socket proxy client, using tcp sockets to relay commands");
    }
}
