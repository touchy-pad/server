package touchy.pad;

/**
 * Creates and starts servers and creates clients.
 * 
 * The getAndStartServer method  
 * 
 * The getClient method should be called on the client side of the application,
 * running on the mobile device.
 * 
 * @author Jan Groothuijse
 */
public interface ProxyProvider {
	/**
	 * Creates a server, which begins listener and returns it. Should be called
	 * on the server side of the application, running on the PC.
	 * 
	 * Please use try-with-resources to manage the resources of the server.
	 * 
	 * @return a running server.
	 */
	TouchLink.ServerProxy getAndStartServer(TouchLink.Backend backEnd);
	
	/**
	 * Creates a new client and connects to the server. Should be called on 
	 * the client side of the application, running on the mobile device.
	 * 
	 * Please use try-with-resources to manage the resources of the client.
	 * 
	 * @return a client proxy, connected to the server.
	 */
	TouchLink.ClientProxy getClient();
}
