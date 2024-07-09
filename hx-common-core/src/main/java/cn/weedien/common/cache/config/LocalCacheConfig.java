package cn.weedien.common.cache.config;

import cn.weedien.common.config.CommonAutoConfiguration;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@ImportAutoConfiguration(CommonAutoConfiguration.class)
public class LocalCacheConfig {

    @Bean("localCache")
    public Cache<String, String> caffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .initialCapacity(100)
                .maximumSize(1000)
                .build();
    }
}
