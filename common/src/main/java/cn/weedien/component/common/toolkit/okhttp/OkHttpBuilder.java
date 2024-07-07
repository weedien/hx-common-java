package cn.weedien.component.common.toolkit.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class OkHttpBuilder {

    /**
     * 同步阻塞等待执行结果的时间，单位毫秒
     */
    private static final Integer DEFAULT_WAIT_TIMEOUT = 3000;

    /**
     * 服务启动时初始化OkHttpClient对象，确保客户端对象单例
     */
    private static final OkHttpClient okHttpClient = OkHttpFactory.newInstance().build();

    private OkHttpBuilder() {
    }


    static {
        // 向JVM注册一个关闭钩子，当服务准备停止时，等待 OkHttpClient 中任务执行完毕再停止，防止线程池中正在执行的任务突然中断
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            final Logger logger = LoggerFactory.getLogger(OkHttpClient.class);
            int count = 100;
            Dispatcher dispatcher = okHttpClient.dispatcher();
            logger.info("ShutdownHook start: queuedCallsCount {} , runningCallsCount {}", dispatcher.queuedCallsCount(), dispatcher.runningCallsCount());
            while (dispatcher.queuedCallsCount() > 0 || dispatcher.runningCallsCount() > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    logger.error("ShutdownHook interrupted: queuedCallsCount {} , runningCallsCount {}", dispatcher.queuedCallsCount(), dispatcher.runningCallsCount());
                    break;
                }
                // 防止无限循环
                if (--count == 0) {
                    logger.error("ShutdownHook timeout: queuedCallsCount {} , runningCallsCount {}", dispatcher.queuedCallsCount(), dispatcher.runningCallsCount());
                    break;
                }
            }
            logger.info("ShutdownHook end: queuedCallsCount {} , runningCallsCount {}", dispatcher.queuedCallsCount(), dispatcher.runningCallsCount());
        }));
    }


    /**
     * 同步执行请求，公共方法
     *
     * @param request 请求对象
     * @return 请求结果
     */
    public static OkHttpResult syncRequest(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            return buildResponseResult(request, response);
        } catch (Exception e) {
            log.error("request error, request:{}", request, e);
            return new OkHttpResult(false, 500, "request error");
        }
    }


    /**
     * 异步执行请求，同步阻塞编程等待返回结果
     * 此方式在多线程环境下请求处理依然能保持高性能，根据不同的场景显式对ConnectionPool进行调优处理
     *
     * @param request 请求对象
     * @return 请求结果
     */
    public static OkHttpResult syncResponse(Request request) {
        if (log.isDebugEnabled()) {
            log.debug("request start, request:{}", request);
        }
        // 使用异步编程，在指定时间内阻塞获取 OKHttp 异步执行结果
        CompletableFuture<Response> completableFuture = new CompletableFuture<>();
        // 发起异步请求调用
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                completableFuture.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                completableFuture.complete(response);
            }
        });
        // 这里的异步执行结果等待时间，取决于get同步获取时间的设定，而不是全局请求超时配置
        try (Response response = completableFuture.get(DEFAULT_WAIT_TIMEOUT, TimeUnit.MILLISECONDS)) {
            return buildResponseResult(request, response);
        } catch (TimeoutException e) {
            call.cancel();
            log.error("request timeout, request:{}", request, e);
            return new OkHttpResult(false, 500, "request timeout");
        } catch (Exception e) {
            log.error("request error, request:{}", request, e);
            return new OkHttpResult(false, 500, "request error");
        }
    }

    /**
     * 封装返回值
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return 请求结果
     * @throws IOException IO异常
     */
    private static OkHttpResult buildResponseResult(Request request, Response response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("request end, request:{}, response:{}", request, response);
        }
        ResponseBody responseBody = response.body();
        OkHttpResult result = OkHttpResult.builder()
                .success(response.isSuccessful())
                .code(response.code())
                .message(response.message())
                .headers(response.headers().toMultimap())
                .body(Objects.nonNull(responseBody) ? responseBody.bytes() : null)
                .build();
        if (result.failed()) {
            log.warn("request fail, request:{}, response:{}", request, response);
        }
        return result;
    }
}