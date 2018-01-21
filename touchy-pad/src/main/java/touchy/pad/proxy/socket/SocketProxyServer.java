package touchy.pad.proxy.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import touchy.pad.TouchLink;

/**
 * Implementation using ethernet.
 * 
 * @author Jan Groothuijse
 */
@Slf4j
public class SocketProxyServer implements TouchLink.ServerProxy, Runnable {
	/**
	 * In production this will hold the actual implementation mousing a pointer.
	 */
	@Delegate
	private final TouchLink.Backend backend;
	
	/**
	 * Reference so run() and stop() have access.
	 */
	private final ServerSocket serverSocket;
	
	SocketProxyServer(final SockerProxyServerConfig config, 
			final TouchLink.Backend upstream) throws IOException {
		backend = upstream;
		log.info("Creating server socket on port: " + config.getPort());
		serverSocket = new ServerSocket(config.getPort());
		log.info("Starting thread to accept connections.");
		new Thread(this).start(); // calls run in a new thread.
	}
	
	public interface SockerProxyServerConfig {
		int getPort();
	}

	@Override
	public final void run() {
		// TODO Auto-generated method stub
		try {
			log.info("Listening on socket server.");
			// Keep listening untill an exception occurs.
			while (true) {
				// Blocking method, will stall the thread.
				final Socket socket = serverSocket.accept();
				log.info("Socket opened.");
				// Create a new thread, just for the connection with this client.
				new Thread(() -> handleConnection(socket)).start();
			}
		} catch (IOException e) {
			log.info("Somebody closed to socket, killing thread that listened "
					+ "on it.");
		}
	}
	
	private final void handleConnection(final Socket socket) {
		log.info("handleConnection called");
		try (
			final ObjectOutputStream output 
				= new ObjectOutputStream(socket.getOutputStream())) {

			// Flush to unfreeze the ObjectInputStream on the other side, see
			// new ObjectInputStream().
			output.flush();
			
			try (final ObjectInputStream input
					= new ObjectInputStream(socket.getInputStream());
			) {
				// While the connection is open, we expect the client to send
				// a method proxy and wiat for the result to be returned.
				while (!socket.isClosed()) {
					log.info("Waiting for client input");
					// Allow the client to send what needs to be done.
					try {
						final MethodProxy methodProxy;					
						methodProxy = (MethodProxy) input.readObject();
						// Return the result of
						final Object result = methodProxy.apply(backend);
						output.writeObject(result);
					} catch (ClassNotFoundException e) {
						log.error("Dropping message received from client"
								+ ", writing back a null value", e);
						output.writeObject(null);
					}
				}
				log.info("Socket is closed.");
			}
			
		} catch (IOException e1) {
			log.error("Network error, closing socket", e1);
			try {
				socket.close();
			} catch (IOException e2) {
				log.error("Failed to close the socket.");
			}
		}
	}

	@Override
	public void close() {
		// Closing the socket causes all blocked accept() method calls to return
		// with a socket exception.
		try {
			log.info("Closing server socket");
			serverSocket.close();
		} catch (IOException e) {
			log.error("Error closing server socket", e);
		}
	}
}
