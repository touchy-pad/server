package touchy.pad.settings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.aop.support.AopUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import touchy.pad.RuntimeConfiguration;
import touchy.pad.UserConfiguration;

/**
 * Groups settings so that the user gets some context.
 *
 * @author Jan Groothuijse
 */
@Getter
@AllArgsConstructor
public class SettingsGroup {
    /**
     * Label used to lookup both name and description of the settings group.
     */
    private final String message;
    /**
     * Settings in this group.
     */
    private final List<Setting> settings;

    /**
     * Configuration.
     */
    private final UserConfiguration config;

    /**
     * @param c Configuration
     */
    SettingsGroup(final UserConfiguration c) {
        this.config = c;
        message = c.getMessage();
        settings = Arrays.stream(AopUtils.getTargetClass(c).getMethods()) //
                .filter(m -> m
                        .getAnnotation(RuntimeConfiguration.class) != null)
                .map(Setting::new).collect(Collectors.toList());
    }
}
