package touchy.pad.connectivity.socket;

import org.springframework.stereotype.Component;

/**
 * Dummy implementation, the interface and an aspect to the actual work.
 *
 * @author Jan Groothuijse
 */
@Component("socketClientProxyConfigImpl")
public class SocketClientConfigImpl implements SocketClientConfig {

    @Override
    public final String getMessage() {
        return "tcpClient";
    }

}
