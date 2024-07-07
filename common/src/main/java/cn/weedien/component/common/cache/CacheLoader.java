package cn.weedien.component.common.cache;

/**
 * 缓存加载器
 *
 */
@FunctionalInterface
public interface CacheLoader<T> {

    /**
     * 加载缓存
     */
    T load();
}
