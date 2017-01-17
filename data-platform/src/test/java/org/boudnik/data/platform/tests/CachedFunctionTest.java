package org.boudnik.data.platform.tests;

import org.boudnik.data.platform.Cache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 01/03/17 15:47
 */
public class CachedFunctionTest {

    public static void main(String[] args) {
        Cache cache = new Cache(new HashMap<>());

        List subList = cache.execute(to -> Arrays.asList(1, 2, 3, 4).subList(1, to), 3);
        System.out.println("subList = " + subList);

        List<String> strings = cache.execute(s -> Arrays.asList(s.split(" ")), "Hello, World!");
        System.out.println("strings = " + strings);

        Function<Object, Integer> fortyTwo = o -> 42;

        Integer execute = cache.execute(fortyTwo, 15);
        System.out.println("execute = " + execute);
        Integer execute1 = cache.execute(fortyTwo, 15);
        System.out.println("execute1 = " + execute1);
        Integer execute2 = cache.execute(fortyTwo, "15");
        System.out.println("execute2 = " + execute2);

        Integer execute3 = cache.execute(ll -> 42, 1, 2, "15");
        System.out.println("execute3 = " + execute3);

        cache.execute(Function.identity(), (Object) null);
        cache.execute(Function.identity(), (Object[]) new String[0]);

        System.out.println("cache =\n" + cache);
        System.out.println();
    }
}
