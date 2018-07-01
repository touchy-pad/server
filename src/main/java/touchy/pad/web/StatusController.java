package touchy.pad.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import touchy.pad.ProxyServerService;
import touchy.pad.TouchLink.Backend;
import touchy.pad.TouchLink.ServerProxy;

/**
 * Allows the user to see the status of the application using a browser.
 *
 * @author Jan Groothuijse
 */
@Controller
@RequestMapping("/")
@AllArgsConstructor
public class StatusController {
    /**
     * Supplies us with a service to query for information.
     */
    private final ProxyServerService proxyServerService;

    /**
     * @return view and model to render a page displaying the current status of
     *         the application
     */
    @RequestMapping("/")
    public final ModelAndView view() {
        final ModelAndView viewModel = new ModelAndView("status");
        final ServerProxy server = proxyServerService.getProxyServer();
        viewModel.addObject("serverStatus", server.getDescription());
        final Backend backEnd = proxyServerService.getBackEnd();
        viewModel.addObject("backEndStatus", backEnd.getDescription());
        return viewModel;
    }
}
