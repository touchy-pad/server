package touchy.pad.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import touchy.pad.RuntimeConfiguration;
import touchy.pad.UserConfiguration;

/**
 * Allows insight into configuration via the web interface.
 *
 * @author Jan Groothuijse
 */
@Controller
@Slf4j
@AllArgsConstructor
public class ConfigurationController {

    /**
     * All configuration beans.
     */
    private final List<UserConfiguration> configurations;

    /**
     * @return view and model to render a page displaying the current status of
     *         the application
     */
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public final ModelAndView view() {
        log.info("handling config GET request");
        final ModelAndView viewModel = new ModelAndView("status");
        final List<Pair<String, List<Pair<String, Object>>>> list =
                new ArrayList<>();
        configurations.forEach(config -> {
            final Class<?> clazz = AopUtils.getTargetClass(config);

            final List<Pair<String, Object>> settings;
            settings = Arrays.stream(clazz.getMethods()) //
                    .map(m -> Pair.of(m,
                            m.getAnnotation(RuntimeConfiguration.class)))
                    .filter(p -> p.getRight() != null) //
                    .map(p -> {
                        try {
                            final Object value = p.getLeft().invoke(config);
                            return Pair.of(p.getRight().value(), value);
                        } catch (IllegalAccessException
                                | IllegalArgumentException
                                | InvocationTargetException e) {
                            log.error("Failed to get configuration value", e);
                            return Pair.of(p.getRight().value(), null);
                        }
                    }).collect(Collectors.toList());
            list.add(Pair.of(clazz.getName(), settings));
            log.info("config: {}", clazz.getName());
        });
        viewModel.addObject("configurations", list);
        return viewModel;
    }
}
