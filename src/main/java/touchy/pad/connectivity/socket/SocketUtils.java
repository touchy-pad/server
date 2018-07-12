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

/**
 * Allows to test exception handling.
 *
 * @author Jan Groothuijse
 */
public interface SocketUtils {

    /**
     * @param host name or ip, ipv4 or ipv6.
     * @return the corresponding address object.
     * @throws UnknownHostException when the host is not known.
     */
    InetAddress addressByName(String host) throws UnknownHostException;

    /**
     * @return A DatagramSocket.
     * @throws SocketException when the socket cannot be created.
     */
    DatagramSocket datagramSocket() throws SocketException;

    /**
     * @param port port number to bind the socket to.
     * @param address internet address to bind the socket to.
     * @return A Datagram socket.
     * @throws SocketException when the socket cannot be created.
     */
    DatagramSocket datagramSocket(int port, InetAddress address)
            throws SocketException;

    /**
     * @param inputStream the input stream to wrap, use.
     * @return A ObjectInputStream that reads from the inputStream.
     * @throws IOException when an IO error occurs.
     */
    ObjectInputStream objectInputStream(InputStream inputStream)
            throws IOException;

    /**
     * @param outputStream the output stream to wrap, use.
     * @return A ObjectOutputStream that write to the outputStream.
     * @throws IOException when an IO error occurs.
     */
    ObjectOutputStream objectOutputStream(OutputStream outputStream)
            throws IOException;

    /**
     * @param port port number to bind the socket to.
     * @param address internet address to bind the socket to.
     * @return A socket.
     * @throws IOException when an IO error occurs.
     */
    Socket socket(int port, InetAddress address) throws IOException;
}
