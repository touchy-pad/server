package touchy.pad.backend;

import java.awt.Point;
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
public final class NoTouchLink implements TouchLink {

    @Override
    public Supplier<Point> move(final Point delta, final boolean left,
            final boolean middle, final boolean right) {
        return null;
    }

    @Override
    public void scroll(final int amount) {

    }

    @Override
    public void sendClipboard(final String text) {

    }

    @Override
    public Supplier<String> receiveClipboard() {
        return null;
    }

}
