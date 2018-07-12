package touchy.pad.connectivity.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Checks that exceptions are handled correctly.
 *
 * @author Jan Groothuijse
 */
public final class SocketClientExceptionHandler {
    /**
     * @throws Exception when the test fails.
     */
    @Test
    public void testIOExceptionInClose() throws Exception {
        final SocketClientConfig config = new SocketClientConfig() {

            @Override
            public String getMessage() {
                return null;
            }

        };

        final SocketUtils socketUtils = Mockito.mock(SocketUtils.class);
        final Socket clientSocket = Mockito.mock(Socket.class);
        final ObjectOutputStream oos = Mockito.mock(ObjectOutputStream.class);
        final ObjectInputStream ios = Mockito.mock(ObjectInputStream.class);

        Mockito.when(socketUtils.objectInputStream(null)).thenReturn(ios);
        Mockito.when(socketUtils.socket(config.getPort(), null))
                .thenReturn(clientSocket);
        Mockito.when(socketUtils.objectOutputStream(null)).thenReturn(oos);

        Mockito.doThrow(IOException.class).when(oos).close();
        final DiscoveredSocketServer touchLink;
        touchLink = Mockito.mock(DiscoveredSocketServer.class);
        final SocketClient client =
                new SocketClient(config, touchLink, socketUtils);
        client.close();
    }

    /**
     * @throws Exception when the test fails.
     */
    @Test
    public void testIOExceptionInWrite() throws Exception {
        final SocketClientConfig config = new SocketClientConfig() {

            @Override
            public String getMessage() {
                return null;
            }

        };

        final SocketUtils socketUtils = Mockito.mock(SocketUtils.class);
        final Socket clientSocket = Mockito.mock(Socket.class);
        final ObjectOutputStream oos = Mockito.mock(ObjectOutputStream.class);
        final ObjectInputStream ios = Mockito.mock(ObjectInputStream.class);

        Mockito.when(socketUtils.objectInputStream(null)).thenReturn(ios);
        Mockito.when(socketUtils.socket(config.getPort(), null))
                .thenReturn(clientSocket);
        Mockito.when(socketUtils.objectOutputStream(null)).thenReturn(oos);

        final String object = "";
        Mockito.when(ios.readObject()).thenReturn(object);
        Mockito.doThrow(IOException.class).when(oos)
                .writeObject(Mockito.any(MethodProxy.class));
        final DiscoveredSocketServer touchLink;
        touchLink = Mockito.mock(DiscoveredSocketServer.class);
        final SocketClient client =
                new SocketClient(config, touchLink, socketUtils);

        assertEquals(object, client.receiveClipboard().get());
        client.close();
    }

    /**
     * @throws Exception when the test fails.
     */
    @Test
    public void testIOExceptionInRead() throws Exception {
        final SocketClientConfig config = new SocketClientConfig() {

            @Override
            public String getMessage() {
                return null;
            }

        };

        final SocketUtils socketUtils = Mockito.mock(SocketUtils.class);
        final Socket clientSocket = Mockito.mock(Socket.class);
        final ObjectOutputStream oos = Mockito.mock(ObjectOutputStream.class);
        final ObjectInputStream ios = Mockito.mock(ObjectInputStream.class);

        Mockito.when(socketUtils.objectInputStream(null)).thenReturn(ios);
        Mockito.when(socketUtils.socket(config.getPort(), null))
                .thenReturn(clientSocket);
        Mockito.when(socketUtils.objectOutputStream(null)).thenReturn(oos);

        Mockito.doThrow(IOException.class).when(ios).readObject();
        final DiscoveredSocketServer touchLink;
        touchLink = Mockito.mock(DiscoveredSocketServer.class);
        final SocketClient client =
                new SocketClient(config, touchLink, socketUtils);
        assertNull(client.receiveClipboard().get());
        client.close();
    }

    /**
     * @throws Exception when the test fails.
     */
    @Test
    public void testClassNotFoundExceptionInRead() throws Exception {
        final SocketClientConfig config = new SocketClientConfig() {

            @Override
            public String getMessage() {
                return null;
            }

        };

        final SocketUtils socketUtils = Mockito.mock(SocketUtils.class);
        final Socket clientSocket = Mockito.mock(Socket.class);
        final ObjectOutputStream oos = Mockito.mock(ObjectOutputStream.class);
        final ObjectInputStream ios = Mockito.mock(ObjectInputStream.class);

        Mockito.when(socketUtils.objectInputStream(null)).thenReturn(ios);
        Mockito.when(socketUtils.socket(config.getPort(), null))
                .thenReturn(clientSocket);
        Mockito.when(socketUtils.objectOutputStream(null)).thenReturn(oos);

        Mockito.doThrow(ClassNotFoundException.class).when(ios).readObject();
        final DiscoveredSocketServer touchLink;
        touchLink = Mockito.mock(DiscoveredSocketServer.class);
        final SocketClient client =
                new SocketClient(config, touchLink, socketUtils);
        assertNull(client.receiveClipboard().get());
        client.close();
    }
}
