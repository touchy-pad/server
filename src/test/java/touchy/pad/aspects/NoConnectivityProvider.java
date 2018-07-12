package touchy.pad.aspects;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import touchy.pad.ConnectivityInitializationException;
import touchy.pad.ConnectivityProvider;
import touchy.pad.ConnectivityProvider.DiscoveredServer;
import touchy.pad.TouchLink.Backend;
import touchy.pad.TouchLink.ClientProxy;
import touchy.pad.TouchLink.ServerProxy;

/**
 * Does not provide a proxy, mock class.
 *
 * @author Jan Groothuijse
 *
 * @param <E> class of the discovered proxy
 */
@Profile("noProxyProvider")
@Component
class NoConnectivityProvider<E extends DiscoveredServer>
        implements ConnectivityProvider<E> {

    @Override
    public ServerProxy getAndStartServer(final Backend backEnd)
            throws ConnectivityInitializationException {
        return null;
    }

    @Override
    public ClientProxy getClient(final E connectTo)
            throws ConnectivityInitializationException {
        return null;
    }

    @Override
    public CloseableQueueProvider<E> discoverServers() {
        return null;
    }

}
