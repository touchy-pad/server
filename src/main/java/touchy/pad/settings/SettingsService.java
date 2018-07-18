package touchy.pad.settings;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import touchy.pad.RuntimeConfigurationBackend;
import touchy.pad.UserConfiguration;

/**
 * Service to query and use settings.
 *
 * @author Jan Groothuijse
 */
@Getter
@Slf4j
@Component
public class SettingsService {

    /**
     * All configuration beans.
     */
    private final List<SettingsGroup> settings;

    private final RuntimeConfigurationBackend backEnd;

    private final Map<Setting, UserConfiguration> settingsToConfiguration;

    /**
     * @param configurations all active configuration objects.
     */
    public SettingsService(List<UserConfiguration> configurations,
            RuntimeConfigurationBackend b) {
        final List<Pair<UserConfiguration, SettingsGroup>> configAndGroups;
        configAndGroups = configurations.stream() //
                .map(config -> Pair.of(config, new SettingsGroup(config))) //
                .collect(Collectors.toList());
        settings = configAndGroups.stream() //
                .map(Pair::getValue) //
                .collect(Collectors.toList());
        settingsToConfiguration = configAndGroups.stream() //
                .<Pair<Setting, UserConfiguration>>flatMap(pair -> pair
                        .getValue().getSettings().stream() //
                        .map(setting -> Pair.<Setting,
                                UserConfiguration>of(setting, pair.getKey())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        backEnd = b;
    }

    public Object getValue(Setting setting) {
        try {
            return setting.getMethod()
                    .invoke(settingsToConfiguration.get(setting));
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            log.info("Failed to get setting " + setting.getMessage());
            return null;
        }
    }

    public void setValue(Setting setting, Object value) {
        setting.getSetter().accept(backEnd, value);
    }
}
