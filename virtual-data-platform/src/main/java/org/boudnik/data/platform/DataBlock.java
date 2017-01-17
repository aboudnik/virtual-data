package org.boudnik.data.platform;

import java.net.URL;
import java.util.Map;

/**
 * Represents quantum portion of congenerous data limited in some of its dimensions
 *
 * @author Alexandre_Boudnik
 * @since 01/11/2017
 */
public class DataBlock {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final URL url;
    private final Map<String, Limitation> limits;

    public DataBlock(URL url, Map<String, Limitation> limits) {
        this.url = url;
        this.limits = limits;
    }

    public Limitation getLimitation(String dimension) {
        return limits.get(dimension);
    }

    public static abstract class Limitation {

        public abstract boolean hasCommon(Limitation other);

        public static class Value extends Limitation {
            Comparable value;

            public Value(Comparable value) {
                this.value = value;
            }

            @Override
            public boolean hasCommon(Limitation other) {
                if (other instanceof Value) {
                    return overlaps(value, value, ((Value) other).value, ((Value) other).value);
                } else if (other instanceof Range) {
                    return overlaps(value, value, ((Range) other).min, ((Range) other).max);
                }
                return false;
            }
        }

        public static class Range extends Limitation {
            Comparable min;
            Comparable max;

            public Range(Comparable min, Comparable max) {
                this.min = min;
                this.max = max;
            }

            @Override
            public boolean hasCommon(Limitation other) {
                if (other instanceof Value) {
                    return other.hasCommon(this);
                } else if (other instanceof Range) {
                    return overlaps(min, max, ((Range) other).min, ((Range) other).max);
                }
                return false;
            }
        }

        @SuppressWarnings("unchecked")
        static boolean overlaps(Comparable minA, Comparable maxA, Comparable minB, Comparable maxB) {
            return !(minA.compareTo(maxB) > 0 || maxA.compareTo(minB) < 0);
        }
    }
}

