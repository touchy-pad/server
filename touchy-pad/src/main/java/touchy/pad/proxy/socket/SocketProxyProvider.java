package touchy.pad.proxy.socket;

import java.io.IOException;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import touchy.pad.ProxyInitializationException;
import touchy.pad.ProxyProvider;
import touchy.pad.TouchLink;
import touchy.pad.TouchLink.ClientProxy;
import touchy.pad.TouchLink.ServerProxy;

/**
 * Provides proxy using the Socket Proxy implementations.
 *
 * While this is a singleton, both the mobile device and the server running on
 * the pc will have a different version.
 *
 * @author Jan Groothuijse
 */
@RequiredArgsConstructor
@Component("socketProxy")
public final class SocketProxyProvider implements ProxyProvider {

    /**
     * Server config.
     */
    private final SocketProxyServerConfig serverConfig;

    /**
     * Client config.
     */
    private final SocketProxyClientConfig clientConfig;

    @Override
    public ServerProxy getAndStartServer(final TouchLink.Backend backEnd)
            throws ProxyInitializationException {

        try {
            return new SocketProxyServer(serverConfig, backEnd);
        } catch (IOException e) {
            // Handle these errors in the interface, so that the user may
            // change config and/or retry.
            throw new ProxyInitializationException(e);
        }
    }

    @Override
    public ClientProxy getClient() throws ProxyInitializationException {
        try {
            return new SocketProxyClient(clientConfig);
        } catch (IOException e) {
            // Handle these errors in the interface, so that the user may
            // change config and/or retry.
            throw new ProxyInitializationException(e);
        }
    }

}
