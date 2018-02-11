package touchy.pad.proxy.socket;

import touchy.pad.ProxyProvider;

/**
 * Stores information required to: 1) Allow the user to choose the right
 * server 2) Connect to the server the user choose.
 * 
 * @author Jan Groothuijse
 */
class DiscoveredProxyServer
        implements ProxyProvider.DiscoveredServer {

    @Override
    public String getName() {
        // TODO Implement DiscoveredProxyServer.getName
        return "";
    }

    @Override
    public String getSpecification() {
        // TODO implement DiscoveredProxyServer.getSpecification
        return "";
    }
}