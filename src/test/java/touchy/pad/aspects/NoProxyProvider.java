package touchy.pad.aspects;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import touchy.pad.ProxyInitializationException;
import touchy.pad.ProxyProvider;
import touchy.pad.ProxyProvider.DiscoveredServer;
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
class NoProxyProvider<E extends DiscoveredServer> implements ProxyProvider<E> {

    @Override
    public ServerProxy getAndStartServer(final Backend backEnd)
            throws ProxyInitializationException {
        return null;
    }

    @Override
    public ClientProxy getClient(final E connectTo)
            throws ProxyInitializationException {
        return null;
    }

    @Override
    public CloseableQueueProvider<E> discoverServers() {
        return null;
    }

}
