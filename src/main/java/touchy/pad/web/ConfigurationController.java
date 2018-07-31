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
                                return new SettingModel(//
                                        getId(group, setting), //
                                        setting.getMessage(), //
                                        value,
                                        value.getClass().getSimpleName());
                            }).collect(Collectors.toList()));
                }).collect(Collectors.toList()));
    }

    /**
     * @param group the setting belongs to
     * @param setting to get the id of
     * @return some identifier, so we can encode which setting to alter in a
     * string.
     */
    private String getId(final SettingsGroup group, final Setting setting) {
        return group.getMessage() + "." + setting.getMessage();
    }

    /**
     * A controllers view of a setting.
     *
     * @author Jan Groothuijse
     */
    @Getter
    @AllArgsConstructor
    public static final class SettingModel {
        /**
         * To identify the settings, used when the user wants to change a
         * setting via post request.
         */
        private final String id;
        /**
         * To translate name and description.
         */
        private final String message;
        /**
         * The current value of the setting, may be the default.
         */
        private final Object value;
        /**
         * The type of the setting, so that the user interface can display the
         * value correctly and create a form to edit it accordingly.
         */
        private final String type;
    }
}
