package touchy.pad.proxy.socket;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Function;

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
public class SocketProxyClient implements TouchLink.ClientProxy, AutoCloseable {

    /**
     * Reference to close.
     */
    private final Socket client;

    /**
     * Reference to close.
     */
    private final SendReceive sendReceive;

    public SocketProxyClient(final SocketProxyClientConfig config)
            throws UnknownHostException, IOException {
        log.info("Creating client connection.");

        client = new Socket(config.getHost(), config.getPort());
        final ObjectOutputStream output;
        output = new ObjectOutputStream(client.getOutputStream());
        // Flush to unfreeze the ObjectInputStream on the other side, see new
        // ObjectInputStream().
        output.flush();
        final ObjectInputStream input;
        input = new ObjectInputStream(client.getInputStream());

        sendReceive = new SendReceive(output, input);
    }

    /**
     * Template pattern applied for sending and receiving while holding a lock.
     * 
     * @author Jan Groothuijse
     */
    @RequiredArgsConstructor
    private static final class SendReceive
            implements AutoCloseable, Function<MethodProxy, Object> {

        /**
         * Reference to close.
         */
        private final ObjectOutputStream output;

        /**
         * Reference to close.
         */
        private final ObjectInputStream input;

        @Override
        public Object apply(MethodProxy proxy) {
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
        }

    }

    @Override
    public Point move(final Point delta, final boolean left,
            final boolean middle, final boolean right) {
        return (Point) sendReceive
                .apply(new MethodProxy.Move(delta, right, right, right));
    }

    @Override
    public void scroll(final int amount) {
        sendReceive.apply(new MethodProxy.Scroll(amount));
    }

    @Override
    public void typeText(final String text) {
        sendReceive.apply(new MethodProxy.TypeText(text));
    }

    @Override
    public String getClipboard() {
        return (String) sendReceive.apply(new MethodProxy.GetClipboard());
    }

    @Override
    public void close() throws Exception {
        sendReceive.close();
        client.close();
    }
}
