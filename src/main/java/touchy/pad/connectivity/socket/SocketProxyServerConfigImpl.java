package touchy.pad.connectivity.socket;

import org.springframework.stereotype.Component;

/**
 * Dummy implementation, the interface and an aspect to the actual work.
 *
 * @author Jan Groothuijse
 */
@Component("socketProxyServerConfig")
public class SocketProxyServerConfigImpl implements SocketServerConfig {

    @Override
    public final String getMessage() {
        return "tcpServer";
    }

}
