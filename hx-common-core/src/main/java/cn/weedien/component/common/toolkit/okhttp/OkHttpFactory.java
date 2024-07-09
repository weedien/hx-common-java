package cn.weedien.component.common.toolkit.okhttp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OkHttpFactory {

    /**
     * 默认允许的最大并发数
     */
    private final static int concurrentSize = 100;

    /**
     * 实例化客户端配置
     *
     * @return OkHttpClient.Builder
     */
    public static OkHttpClient.Builder newInstance() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        // 设置超时时间
        httpClientBuilder.connectTimeout(5, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(5, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(5, TimeUnit.SECONDS);

        // 设置连接池，连接池的最大数量=并发数，30秒空闲的连接会释放掉
        httpClientBuilder.connectionPool(new ConnectionPool(concurrentSize, 30, TimeUnit.SECONDS));

        // 设置分发器处理的最大容量，这里的数量=连接池数量=并发数，表示此客户端有同时能够处理xx并发数的能力
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(concurrentSize);
        dispatcher.setMaxRequestsPerHost(concurrentSize);
        httpClientBuilder.dispatcher(dispatcher);

        return httpClientBuilder;
    }

}