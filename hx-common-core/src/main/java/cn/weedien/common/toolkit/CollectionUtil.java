package cn.weedien.common.toolkit;


import cn.weedien.common.constant.MagicNumberConstants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Collection util.<br>
 * Refer to cn.hutool.core.collection.CollUtil:<br>
 */
public class CollectionUtil {

    /**
     * Get first.
     *
     * @param iterable
     * @param <T>
     * @return
     */
    public static <T> T getFirst(Iterable<T> iterable) {
        if (iterable != null) {
            Iterator<T> iterator = iterable.iterator();
            if (iterator != null && iterator.hasNext()) {
                return iterator.next();
            }
        }
        return null;
    }

    /**
     * Is empty.
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * Is not empty.
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }

    /**
     * Is empty.
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Is not empty.
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Is empty.
     *
     * @param iterator
     * @return
     */
    public static boolean isEmpty(Iterator<?> iterator) {
        return null == iterator || !iterator.hasNext();
    }

    /**
     * Is not empty.
     *
     * @param iterator
     * @return
     */
    public static boolean isNotEmpty(Iterator<?> iterator) {
        return !isEmpty(iterator);
    }

    /**
     * Is empty.
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Is not empty.
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * to list
     *
     * @param ts  elements
     * @param <T> type
     * @return List
     */
    public static <T> List<T> toList(T... ts) {
        if (ts == null || ts.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.stream(ts)
                .collect(Collectors.toList());
    }

    /**
     * reference google guava
     *
     * @param elements
     * @return
     */
    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        Objects.requireNonNull(elements);
        // Avoid integer overflow when a large array is passed in
        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList<>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    private static int computeArrayListCapacity(int arraySize) {
        checkNonnegative(arraySize);
        // TODO(kevinb): Figure out the right behavior, and document it
        return saturatedCast(MagicNumberConstants.LONG_5 + arraySize + (arraySize / MagicNumberConstants.INDEX_10));
    }

    private static void checkNonnegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("arraySize cannot be negative but was: " + value);
        }
    }

    private static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }
}
