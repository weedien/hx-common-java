package cn.weedien.common.toolkit;

import cn.weedien.common.function.Matcher;

import java.lang.reflect.Array;

/**
 * ArrayUtil
 */
public class ArrayUtil {

    /**
     * Is empty.
     *
     * @param array array
     * @param <T>   type
     * @return boolean
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Is not empty.
     *
     * @param array array
     * @param <T>   type
     * @return boolean
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    /**
     * First match.
     *
     * @param matcher matcher
     * @param array   array
     * @param <T>     type
     * @return T
     */
    public static <T> T firstMatch(Matcher<T> matcher, T... array) {
        if (!isEmpty(array)) {
            for (final T val : array) {
                if (matcher.match(val)) {
                    return val;
                }
            }
        }
        return null;
    }

    /**
     * Add all.
     *
     * @param array1 array1
     * @param array2 array2
     * @param <T>    type
     * @return T[]
     */
    public static <T> T[] addAll(final T[] array1, final T... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final Class<?> type1 = array1.getClass().getComponentType();
        @SuppressWarnings("unchecked") final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (final ArrayStoreException ase) {
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of "
                        + type1.getName(), ase);
            }
            throw ase;
        }
        return joinedArray;
    }

    /**
     * Clone.
     *
     * @param array array
     * @param <T>   type
     * @return T[]
     */
    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }
}
