package touchy.pad.web;

import java.awt.Point;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * Move mouse on the server.
     *
     * @param moveRight how many pixels to move the mouse right.
     * @param moveDown how many pixels to move the mouse down.
     * @return the same status view as view() would return.
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public final ModelAndView
            view(final @RequestParam(value = "moveToRight",
                    defaultValue = "0") int moveRight,
                    final @RequestParam(value = "moveToBottom",
                            defaultValue = "0") int moveDown) {
        // Move the mouse on the server
        proxyServerService.getBackEnd().move(new Point(moveRight, moveDown),
                false, false, false);
        // Return same view as view.
        return view();
    }
}
