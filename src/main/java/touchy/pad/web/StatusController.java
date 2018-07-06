package touchy.pad.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public final ModelAndView view() {
        log.info("handling GET request");
        final ModelAndView viewModel = new ModelAndView("view");
        final ServerProxy server = proxyServerService.getProxyServer();
        viewModel.addObject("serverStatus", server.getDescription());
        final Backend backEnd = proxyServerService.getBackEnd();
        viewModel.addObject("backEndStatus", backEnd.getDescription());
        return viewModel;
    }
}
