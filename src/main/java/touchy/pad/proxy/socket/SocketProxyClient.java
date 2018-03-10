package touchy.pad.proxy.socket;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;
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
    private final SendReceive sendReceive;

    /**
     * @param config to get port number and hostname.
     * @param connectTo connect to a server.
     * @throws UnknownHostException when no connection can be made.
     * @throws IOException when the connection fails.
     */
    public SocketProxyClient(final SocketProxyClientConfig config,
            final DiscoveredProxyServer connectTo)
            throws UnknownHostException, IOException {
        log.info("Creating client connection.");

        final Socket client =
                new Socket(connectTo.getAddress(), config.getPort());
        final ObjectOutputStream output;
        output = new ObjectOutputStream(client.getOutputStream());
        log.info("Created client output.");
        // Flush to unfreeze the ObjectInputStream on the other side, see new
        // ObjectInputStream().
        output.flush();
        log.info("Flushed.");
        final ObjectInputStream input;
        input = new ObjectInputStream(client.getInputStream());
        log.info("creating sendReceive.");

        sendReceive = new SendReceive(client, output, input);
        log.info("created sendReceive.");
    }

    /**
     * Template pattern applied for sending and receiving while holding a lock.
     *
     * @author Jan Groothuijse
     */
    @RequiredArgsConstructor
    static final class SendReceive
            implements AutoCloseable, Function<MethodProxy, Object> {

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

        @Override
        public Object apply(final MethodProxy proxy) {
            synchronized (this) {
                try {
                    try {
                        output.writeObject(proxy);
                    } catch (IOException e) {
                        log.error("Network error while writing", e);
                    }
                    return input.readObject();
                } catch (IOException e) {
                    log.error("Network error while reading", e);
                } catch (ClassNotFoundException e) {
                    log.error("Unknown class while reading response from "
                            + "server", e);
                }
                return null;
            }
        }

        @Override
        public void close() throws Exception {
            output.close();
            input.close();
            client.close();
        }

    }

    @Override
    public Supplier<Point> move(final Point delta, final boolean left,
            final boolean middle, final boolean right) {
        final Point point = (Point) sendReceive
                .apply(new MethodProxy.Move(delta, right, right, right));
        return () -> point;
    }

    @Override
    public void scroll(final int amount) {
        sendReceive.apply(new MethodProxy.Scroll(amount));
    }

    @Override
    public void sendClipboard(final String text) {
        sendReceive.apply(new MethodProxy.TypeText(text));
    }

    @Override
    public Supplier<String> receiveClipboard() {
        final String string =
                (String) sendReceive.apply(new MethodProxy.GetClipboard());
        return () -> string;
    }

    @Override
    public void close() throws Exception {
        sendReceive.close();
    }
}
