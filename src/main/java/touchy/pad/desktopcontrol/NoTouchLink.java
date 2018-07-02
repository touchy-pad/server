package touchy.pad.desktopcontrol;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import touchy.pad.TouchLink;

/**
 * No-op implementation.
 *
 * @author Jan Groothuijse
 */
@Profile("noTouchLink")
@Component
public final class NoTouchLink implements TouchLink.Backend {

    @Override
    public Supplier<Point> move(final Point delta, final boolean left,
            final boolean middle, final boolean right) {
        return () -> delta;
    }

    @Override
    public void scroll(final int amount) {
        // No op implementation.
    }

    @Override
    public void sendClipboard(final String text) {
        // No op implementation.
    }

    @Override
    public Supplier<String> receiveClipboard() {
        return () -> "";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("No backend, stub implementation");
    }

}
