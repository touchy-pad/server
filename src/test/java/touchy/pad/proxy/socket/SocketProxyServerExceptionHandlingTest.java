package touchy.pad.proxy.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.mockito.Mockito;

/**
 * White box testing of socket proxy server, aims at coverage.
 *
 * @author Jan Groothuijse
 */
public final class SocketProxyServerExceptionHandlingTest {
    /**
     * @throws IOException when network fails.
     */
    // @Test
    public void handleConnectionGetOutputThrows() throws IOException {
        final SocketProxyServer sut =
                new SocketProxyServer(new SocketProxyServerConfig() {
                }, null, InetAddress.getByName("0.0.0.0"));

        final Socket socket = Mockito.mock(Socket.class);
        Mockito.doThrow(IOException.class).when(socket).getOutputStream();

        sut.handleConnection(socket);
        sut.close();
    }

    /**
     * @throws IOException when network fails.
     */
    // @Test
    public void handleConnectionGetInputThrows() throws IOException {
        final SocketProxyServerConfig config = new SocketProxyServerConfig() {

        };

        final SocketProxyServer sut;
        sut = new SocketProxyServer(config, null,
                InetAddress.getByName("0.0.0.0"));

        final OutputStream os = new ByteArrayOutputStream();
        final Socket socket = Mockito.mock(Socket.class);
        Mockito.doReturn(os).when(socket).getOutputStream();
        Mockito.doThrow(IOException.class).when(socket).getInputStream();

        sut.handleConnection(socket);
        sut.close();
    }
}
