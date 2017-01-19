package org.boudnik.data.platform;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
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

    public DataBlock(URL url, Limitation... limits) {
        this.url = url;
        this.limits = new HashMap<>();
        for (Limitation limit : limits) {
            Limitation replaced = this.limits.put(limit.name, limit);
            assert replaced == null : "Non-unique limitation";
        }
    }

    public Limitation getLimitation(String dimension) {
        return limits.get(dimension);
    }

    public static abstract class Limitation {
        private final String name;

        Limitation(String name) {
            this.name = name;
        }

        public abstract boolean hasCommon(Limitation other);

        public static class Value extends Limitation {
            Comparable value;

            public Value(String name, Comparable value) {
                super(name);
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

            public Range(String name, Comparable min, Comparable max) {
                super(name);
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

