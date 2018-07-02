package touchy.pad.aspects;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import touchy.pad.ProxyInitializationException;
import touchy.pad.ProxyProvider;
import touchy.pad.ProxyProvider.DiscoveredServer;
import touchy.pad.TouchLink.Backend;
import touchy.pad.TouchLink.ClientProxy;
import touchy.pad.TouchLink.ServerProxy;

@Profile("noProxyProvider")
@Component
class NoProxyProvider<E extends DiscoveredServer> implements ProxyProvider<E> {

    @Override
    public ServerProxy getAndStartServer(Backend backEnd)
            throws ProxyInitializationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ClientProxy getClient(E connectTo)
            throws ProxyInitializationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CloseableQueueProvider<E> discoverServers() {
        // TODO Auto-generated method stub
        return null;
    }

}