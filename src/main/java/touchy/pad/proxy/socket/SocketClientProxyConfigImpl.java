package touchy.pad.proxy.socket;

import org.springframework.stereotype.Component;

/**
 * Dummy implementation, the interface and an aspect to the actual work.
 *
 * @author Jan Groothuijse
 */
@Component("socketClientProxyConfigImpl")
public class SocketClientProxyConfigImpl implements SocketProxyClientConfig {

    @Override
    public final String getMessage() {
        return "tcpClient";
    }

}
