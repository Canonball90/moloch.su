package net.spartanb312.base.utils;

import java.util.*;

/**
 * Kotlin would be better than java
 */
public class ListUtil {
    @SafeVarargs
    public static <T> List<T> listOf(T... elements){
        return Arrays.asList(elements);
    }
}
