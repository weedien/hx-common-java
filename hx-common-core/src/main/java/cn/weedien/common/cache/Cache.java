package cn.weedien.common.cache;

import jakarta.annotation.Nonnull;

import java.util.Collection;

/**
 * 缓存接口
 *
 */
public interface Cache {

    /**
     * 获取缓存
     */
    <T> T get(@Nonnull String key, Class<T> clazz);

    /**
     * 放入缓存
     */
    void put(@Nonnull String key, Object value);

    /**
     * 如果 keys 全部不存在，则新增，返回 true，反之 false
     */
    Boolean putIfAllAbsent(@Nonnull Collection<String> keys);
    /**
     * 删除缓存
     */
    Boolean delete(@Nonnull String key);

    /**
     * 删除 keys，返回删除数量
     */
    Long delete(@Nonnull Collection<String> keys);

    /**
     * 判断 key 是否存在
     */
    Boolean hasKey(@Nonnull String key);

    /**
     * 获取缓存组件实例
     */
    Object getInstance();
}