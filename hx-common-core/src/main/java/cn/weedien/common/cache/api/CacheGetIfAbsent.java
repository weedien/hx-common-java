package cn.weedien.common.cache.api;

/**
 * 缓存查询为空
 *
 */
@FunctionalInterface
public interface CacheGetIfAbsent<T> {

    /**
     * 如果查询结果为空，执行逻辑
     */
    void execute(T param);
}
