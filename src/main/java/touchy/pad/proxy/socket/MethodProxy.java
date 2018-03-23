package touchy.pad.proxy.socket;

import java.awt.Point;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import touchy.pad.TouchLink;

/**
 * A class per method of the TouchLink interface, so that the methods calls can
 * be serialized and transmitted.
 *
 * These objects marshall the method calls, using serialization to get across
 * the socket.
 *
 * @author Jan Groothuijse
 */
interface MethodProxy extends Function<TouchLink, Object>, Serializable {

    /**
     * Proxy for a getClipboard method call.
     *
     * @author Jan Groothuijse
     */
    @RequiredArgsConstructor
    final class GetClipboard implements MethodProxy {
        /**
         * For serialization.
         */
        private static final long serialVersionUID = 9025208712220799066L;

        @Override
        public Object apply(final TouchLink arg0) {
            return arg0.receiveClipboard().get();
        }
    }

    /**
     * Proxy for a move method call.
     *
     * @author Jan Groothuijse
     */
    @RequiredArgsConstructor
    final class Move implements MethodProxy {

        /**
         * For serialization.
         */
        private static final long serialVersionUID = 9168276452622252668L;
        /**
         * Movement of the pointer.
         */
        private final Point delta;
        /**
         * Whether the left mouse button was pressed.
         */
        private final boolean left;
        /**
         * Whether the middle mouse button was pressed.
         */
        private final boolean middle;
        /**
         * Whether the right mouse button was pressed.
         */
        private final boolean right;

        @Override
        public Object apply(final TouchLink t) {
            return t.move(delta, left, middle, right).get();
        }
    }

    /**
     * Proxy for a scroll method call.
     *
     * @author Jan Groothuijse
     */
    @RequiredArgsConstructor
    final class Scroll implements MethodProxy {
        /**
         * For serialization.
         */
        private static final long serialVersionUID = 3672412656402588279L;
        /**
         * The amount to scroll down, negative means scrolling up.
         */
        private final int amount;

        @Override
        public Object apply(final TouchLink t) {
            t.scroll(amount);
            return null;
        }

    }

    /**
     * Proxy for a typeText method call.
     *
     * @author Jan Groothuijse
     */
    @RequiredArgsConstructor
    final class TypeText implements MethodProxy {
        /**
         * For serialization.
         */
        private static final long serialVersionUID = -7508455990087416324L;
        /**
         * The text to type in the interface.
         */
        private final String text;

        @Override
        public Object apply(final TouchLink t) {
            Optional.ofNullable(t).ifPresent(o -> o.sendClipboard(text));
            return null;
        }
    }
}
