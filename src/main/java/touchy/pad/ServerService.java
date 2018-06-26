package touchy.pad;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import touchy.pad.ProxyProvider.DiscoveredServer;
import touchy.pad.TouchLink.Backend;

/**
 * Component to manage the proxy server.
 * 
 * @author Jan Groothuijse
 */
@Component
@Slf4j
public class ServerService implements AutoCloseable {

    /**
     * Provides proxy server.
     */
    final ProxyProvider<? extends DiscoveredServer> provider;
    /**
     * The thing being proxied by the proxy server. The proxy server relais the
     * clients commands to this back end.
     */
    final Backend backEnd;

    /**
     * A reference to close, so that connection and other resources are handled
     * appropriatly. Mutable state.
     */
    final AtomicReference<TouchLink.ServerProxy> serverProxy;
    /**
     * Atomic flag to check if we are already restarting. Mutable state.
     */
    final AtomicBoolean restart = new AtomicBoolean(false);

    @Autowired
    ServerService(final ProxyProvider<? extends DiscoveredServer> p,
            final Backend b) throws ProxyInitializationException {
        provider = p;
        backEnd = b;
        log.info("Starting proxy server using provider: {}, backend: {}",
                p.getClass(), b.getClass());
        serverProxy =
                new AtomicReference<>(provider.getAndStartServer(backEnd));
    }

    /**
     * Restarts the proxy server, which ever proxy server is configured.
     * 
     * @throws ProxyInitializationException when starting the server fails.
     */
    public void restart() throws ProxyInitializationException {
        final boolean notRestarting = false;
        final boolean restarting = true;
        // If we are not already restarting, then set restart to true, to
        // indicate we are going to restart.
        if (restart.compareAndSet(notRestarting, restarting)) {
            log.info("Restarting proxy server");
            try {
                serverProxy.get().close();
            } catch (final Exception e) {
                log.warn("Unable to close proxy server, trying to start a new"
                        + " proxy server regardless", e);
            }
            try {
                serverProxy.set(provider.getAndStartServer(backEnd));
            } catch (final ProxyInitializationException e) {
                restart.set(notRestarting);
                log.error("Failed to start proxy server, rethrowing exception.",
                        e);
                // Rethrow so that the calling party is alerted and able to show
                // the user some useful information.
                throw e;
            }
            restart.set(notRestarting);
        } else {
            log.info("Already restarting proxy server, ignoring method call");
        }
    }

    /**
     * For introspction, so that the user may query stuff.
     * 
     * @return the currenty running proxy server
     */
    public TouchLink.ServerProxy getProxyServer() {
        return serverProxy.get();
    }

    @Override
    public void close() throws Exception {
        serverProxy.get().close();
    }
}
