package touchy.pad;

import java.io.Closeable;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

/**
 * Creates and starts servers and creates clients.
 *
 * The getAndStartServer method
 *
 * The getClient method should be called on the client side of the application,
 * running on the mobile device.
 *
 * @author Jan Groothuijse
 *
 * @param <E> The type of the discovered servers. Using a bound instead of
 *            normal inheritance improves the typing, since we do not have to
 *            cast in implementations of touchy.pad.ProxyProvider.getClient(),
 *            to access information not in the DiscoveredServer interface needed
 *            to connect to the server.
 */
public interface ProxyProvider<E extends ProxyProvider.DiscoveredServer> {

    /**
     * Combines lifecycle and queue of discovery.
     *
     * @author Jan Groothuijse
     *
     * @param <E> Type of discovered servers.
     */
    public interface CloseableQueueProvider<
            E extends ProxyProvider.DiscoveredServer>
            extends Supplier<BlockingQueue<E>>, Closeable {

    }

    /**
     * ProxyProvider Creates a server, which begins listener and returns it.
     * Should be called on the server side of the application, running on the
     * PC.
     *
     * Please use try-with-resources to manage the resources of the server.
     *
     * @param backEnd to actually move the pointer, the upstream.
     * @throws ProxyInitializationException when initialization
     * @return a running server.
     */
    TouchLink.ServerProxy getAndStartServer(TouchLink.Backend backEnd)
            throws ProxyInitializationException;

    /**
     * Creates a new client and connects to the server. Should be called on the
     * client side of the application, running on the mobile device.
     *
     * Please use try-with-resources to manage the resources of the client.
     *
     * @param connectTo the discovered server to connect to.
     * @throws ProxyInitializationException when initialization
     * @return a client proxy, connected to the server.
     */
    TouchLink.ClientProxy getClient(E connectTo)
            throws ProxyInitializationException;

    /**
     * Runs a server discovery, on ethernet this would be using broadcast etc.
     * On bluetooth it would look for bluetooth devices in discover modes.
     *
     * @return list of available server, so that the client may choose one.
     */
    CloseableQueueProvider<E> discoverServers();

    /**
     * Type to represent servers that are available to a client. Instances are
     * used to refer to a server to connect to.
     *
     * The proxy provider first provides a list of servers that are available,
     * then the GUI displays this list and lets the user choose one, this
     * choosen server is then connected to using
     * touchy.pad.ProxyProvider.getClient().
     *
     * Derived classes implements some generic methods so that the user can
     * identify the server and make an informed decision to to connect to a
     * specific server. Derived classes implement some unspecified logic so that
     * the getClient method connects to the correct server.
     *
     * Implementors are expected to implement hash.
     *
     * @author Jan Groothuijse
     */
    interface DiscoveredServer {
        /**
         * For human consumption.
         *
         * @return the name of the server.
         */
        String getName();

        /**
         * For humand consumption, but may contain technical details.
         *
         * @return hardware addresses, host names, device keys etc.
         */
        String getSpecification();
    }
}
