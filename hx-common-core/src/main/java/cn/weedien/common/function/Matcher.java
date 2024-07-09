package cn.weedien.common.function;

/**
 * Matcher.
 */
@FunctionalInterface
public interface Matcher<T> {

    /**
     * Returns {@code true} if this matches {@code t}, {@code false} otherwise.
     */
    boolean match(T t);
}