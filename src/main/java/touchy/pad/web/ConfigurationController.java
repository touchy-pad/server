package touchy.pad.web;

import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import touchy.pad.settings.Setting;
import touchy.pad.settings.SettingsGroup;
import touchy.pad.settings.SettingsService;

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
    private final SettingsService settingsService;

    /**
     * @return view and model to render a page displaying the current status of
     *         the application
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public final ModelAndView view() {
        log.info("handling settings GET request");
        return new ModelAndView("settings", "settingsGroups",
                settingsService.getSettings().stream().map(group -> {
                    log.info("Group: {}", group.getMessage());
                    return Pair.of(group.getMessage(),
                            group.getSettings().stream().map(setting -> {
                                log.info("setting: {}", setting.getMessage());
                                final Object value =
                                        settingsService.getValue(setting);
                                return new SettingModel( //
                                        getId(group, setting), //
                                        setting.getMessage(), //
                                        value,
                                        value.getClass().getSimpleName());
                            }).collect(Collectors.toList()));
                }).collect(Collectors.toList()));
    }

    private String getId(SettingsGroup group, Setting setting) {
        return group.getMessage() + "." + setting.getMessage();
    }

    @Getter
    @AllArgsConstructor
    public static final class SettingModel {
        private final String id;
        private final String message;
        private final Object value;
        private final String type;
    }
}
