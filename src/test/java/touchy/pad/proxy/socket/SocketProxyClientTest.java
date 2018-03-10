package touchy.pad.proxy.socket;

import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.junit.Test;
import org.mockito.Mockito;

import touchy.pad.proxy.socket.SocketProxyClient.SendReceive;

/**
 * Tests the socket proxy client, noteably checks some exception handling.
 *
 * @author Jan Groothuijse
 */
public final class SocketProxyClientTest {

    /**
     * @throws Exception when the test fails.
     */
    @Test
    public void checkSendReceiveWriteException() throws Exception {
        final MethodProxy methodProxy = new MethodProxy.GetClipboard();
        final ObjectOutputStream output =
                Mockito.mock(ObjectOutputStream.class);
        Mockito.doThrow(IOException.class).when(output)
                .writeObject(methodProxy);
        final ObjectInputStream input = Mockito.mock(ObjectInputStream.class);
        final Socket socket = Mockito.mock(Socket.class);
        try (SendReceive sut = new SendReceive(socket, output, input)) {
            assertNull(sut.apply(methodProxy));
        }
    }

    /**
     * @throws Exception when the test fails.
     */
    @Test
    public void checkSendReceiveReadExceptionIO() throws Exception {
        final MethodProxy methodProxy = new MethodProxy.GetClipboard();
        final ObjectOutputStream output =
                Mockito.mock(ObjectOutputStream.class);
        final ObjectInputStream input = Mockito.mock(ObjectInputStream.class);
        Mockito.doThrow(IOException.class).when(input).readObject();
        final Socket socket = Mockito.mock(Socket.class);
        try (SendReceive sut = new SendReceive(socket, output, input)) {
            assertNull(sut.apply(methodProxy));
        }
    }

    /**
     * @throws Exception when the test fails.
     */
    @Test
    public void checkSendReceiveReadExceptionClassNotFound() throws Exception {
        final MethodProxy methodProxy = new MethodProxy.GetClipboard();
        final ObjectOutputStream output =
                Mockito.mock(ObjectOutputStream.class);
        final ObjectInputStream input = Mockito.mock(ObjectInputStream.class);
        Mockito.doThrow(ClassNotFoundException.class).when(input).readObject();
        final Socket socket = Mockito.mock(Socket.class);
        try (SendReceive sut = new SendReceive(socket, output, input)) {
            assertNull(sut.apply(methodProxy));
        }
    }
}
