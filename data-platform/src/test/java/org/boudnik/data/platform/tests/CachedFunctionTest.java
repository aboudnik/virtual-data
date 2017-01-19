package org.boudnik.data.platform.tests;

import org.boudnik.data.platform.Cache;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 01/03/17 15:47
 */
public class CachedFunctionTest {

    @Test
    public void main() {
        Cache cache = new Cache(new HashMap<>());

        List subList = cache.execute(to -> Arrays.asList(1, 2, 3, 4).subList(1, to), 3);
        System.out.println("subList = " + subList);

        List<String> strings = cache.execute(s -> Arrays.asList(s.split(" ")), "Hello, World!");
        System.out.println("strings = " + strings);

        Function<Object, Integer> fortyTwo = o -> 42;

        assertEquals((int) cache.execute(fortyTwo, 15), 42);
        assertEquals((int) cache.execute(fortyTwo, 15), 42);
        assertEquals((int) cache.execute(fortyTwo, "15"), 42);

        assertEquals((int) cache.execute(ll -> 42, 1, 2, "15"), 42);
        assertNull(cache.execute(Function.identity(), (Object) null));
        assertArrayEquals(cache.execute(Function.identity(), (Object[]) new String[0]), new String[0]);

        System.out.println("cache =\n" + cache);
        System.out.println();
    }
}
