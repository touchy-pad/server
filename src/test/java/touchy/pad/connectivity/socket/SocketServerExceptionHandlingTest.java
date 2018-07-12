package touchy.pad.connectivity.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * White box testing of socket proxy server, aims at coverage.
 *
 * @author Jan Groothuijse
 */
public final class SocketServerExceptionHandlingTest {
    /**
     * @throws IOException when network fails.
     * @throws InterruptedException when the join call is interrupted.
     */
    @Test
    public void handleConnectionGetOutputThrows()
            throws IOException, InterruptedException {
        final SocketServer sut =
                new SocketServer(new SocketServerConfig() {

                    @Override
                    public String getMessage() {
                        return null;
                    }
                }, null, InetAddress.getByName("0.0.0.0"),
                        new SocketUtilsImpl());

        final Socket socket = Mockito.mock(Socket.class);
        Mockito.doThrow(IOException.class).when(socket).getOutputStream();

        sut.handleConnection(socket);
        sut.close();
    }

    /**
     * @throws IOException when network fails.
     * @throws InterruptedException when the join call is interrupted.
     */
    @Test
    public void handleConnectionGetInputThrows()
            throws IOException, InterruptedException {
        final SocketServerConfig config = new SocketServerConfig() {

            @Override
            public String getMessage() {
                return null;
            }

        };

        final SocketServer sut;
        sut = new SocketServer(config, null,
                InetAddress.getByName("0.0.0.0"), new SocketUtilsImpl());

        final OutputStream os = new ByteArrayOutputStream();
        final Socket socket = Mockito.mock(Socket.class);
        Mockito.doReturn(os).when(socket).getOutputStream();
        Mockito.doThrow(IOException.class).when(socket).getInputStream();

        sut.handleConnection(socket);
        sut.close();
    }
}
