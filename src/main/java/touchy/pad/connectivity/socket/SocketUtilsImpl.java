package touchy.pad.connectivity.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

/**
 * Implementation to be used in production.
 *
 * @author Jan Groothuijse
 */
@Component
public final class SocketUtilsImpl implements SocketUtils {
    @Override
    public InetAddress addressByName(final String host)
            throws UnknownHostException {
        return InetAddress.getByName(host);
    }

    @Override
    public DatagramSocket datagramSocket() throws SocketException {
        return new DatagramSocket();
    }

    @Override
    public DatagramSocket datagramSocket(//
            final int port, //
            final InetAddress address) //
            throws SocketException {
        return new DatagramSocket(port, address);
    }

    @Override
    public ObjectInputStream objectInputStream(final InputStream inputStream)
            throws IOException {
        return new ObjectInputStream(inputStream);
    }

    @Override
    public ObjectOutputStream objectOutputStream(
            final OutputStream outputStream) throws IOException {
        return new ObjectOutputStream(outputStream);
    }

    @Override
    public Socket socket(//
            final int port, //
            final InetAddress address) //
            throws IOException {
        return new Socket(address, port);
    }
}
