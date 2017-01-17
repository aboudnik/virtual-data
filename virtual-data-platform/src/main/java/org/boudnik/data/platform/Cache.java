package org.boudnik.data.platform;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 01/10/17 14:40
 */
public class Cache {
    private final Map<Call, Object> map;


    /**
     * Mark for absent key-values. UUID version 5 for DNS namespace "null.timeline.boudnik.org"
     */
    private final static Object ZILCH = UUID.fromString("d656ccc0-09e2-54a9-97ad-ddf08e0cf17f");

    public Cache(Map<Call, Object> map) {
        this.map = map;
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key.
     * <p>
     *
     * @see Map#get(java.lang.Object)
     */
    private <T, R> R get(Call<T, R> call) {
        @SuppressWarnings("unchecked") R result = (R) map.get(call);
        if (ZILCH.equals(result)) {
            return null;
        } else if (result == null) {
            map.put(call, (result = call.function.apply(call.argument)) == null ? ZILCH : result);
        }
        return result;
    }

    public <T, R> R execute(Function<T, R> function, T argument) {
        return get(new Call<>(function, argument));
    }

    @SuppressWarnings("unchecked")
    public <T, R> R execute(Function<T[], R> function, T... arguments) {
        return get(new Call<>(function, arguments));
    }

    @SuppressWarnings("unused")
    public <T, R> void evict(Call<T, R> key) {
        map.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        map.forEach((call, result) -> sb.append(String.format("%s->%s%n", call, toString(result))));
        return sb.toString();
    }

    static String toString(Object o) {
        if (ZILCH.equals(o))
            return null;
        if (o != null && o.getClass().isArray())
            return Arrays.toString((Object[]) o);
        return String.valueOf(o);
    }

    private static class Call<T, R> {
        private final Function<T, R> function;
        private final T argument;

        Call(Function<T, R> function, T argument) {
            this.function = function;
            this.argument = argument;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Call<?, ?> call = (Call<?, ?>) o;

            return function.equals(call.function) && (argument != null ? argument.equals(call.argument) : call.argument == null);
        }

        @Override
        public int hashCode() {
            return function.hashCode();
        }

        @Override
        public String toString() {
            return function + "(" + Cache.toString(argument) + ")";
        }
    }
}
