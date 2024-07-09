package cn.weedien.common.toolkit.okhttp;

import cn.weedien.common.constant.Constants;
import cn.weedien.common.constant.HttpMediaType;
import cn.weedien.common.toolkit.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OkHttpUtil {


    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> params) {
        return get(url, null, params, null);
    }

    public static <T> T get(String url, Map<String, String> headers, TypeReference<T> responseType) {
        return get(url, headers, null, responseType);
    }

    public static <T> T get(String url, Map<String, String> headers, Map<String, String> params, TypeReference<T> responseType) {
        Request request = new Request.Builder()
                .url(buildUrl(url, params))
                .headers(buildHeaders(headers))
                .get()
                .build();
        OkHttpResult result = commonRequest(request);
        return buildResponse(result, responseType);
    }

    public static String postByForm(String url, Map<String, String> params, Map<String, String> formBody, Map<String, String> headers) {
        Request request = new Request.Builder()
                .url(buildUrl(url, params))
                .headers(buildHeaders(headers))
                .post(buildFormBody(formBody))
                .build();
        OkHttpResult result = commonRequest(request);
        return buildResponse(result);
    }

    public static <T> T postByForm(String url, Map<String, String> params, Map<String, String> formBody, Map<String, String> headers, TypeReference<T> responseType) {
        Request request = new Request.Builder()
                .url(buildUrl(url, params))
                .headers(buildHeaders(headers))
                .post(buildFormBody(formBody))
                .build();
        OkHttpResult result = commonRequest(request);
        return buildResponse(result, responseType);
    }

    public static String postByJson(String url, Object body) {
        return postByJson(url, body, null, null);
    }

    public static <T> T postByJson(String url, Object body, TypeReference<T> responseType) {
        return postByJson(url, body, null, responseType);
    }

    public static String postByJson(String url, Object body, Map<String, String> headers) {
        Request request = new Request.Builder()
                .url(url)
                .headers(buildHeaders(headers))
                .post(buildJsonBody(body))
                .build();
        OkHttpResult result = commonRequest(request);
        return buildResponse(result);
    }

    public static <T> T postByJson(String url, Object body, Map<String, String> headers, TypeReference<T> responseType) {
        Request request = new Request.Builder()
                .url(url)
                .headers(buildHeaders(headers))
                .post(buildJsonBody(body))
                .build();
        OkHttpResult result = commonRequest(request);
        return buildResponse(result, responseType);
    }

    @SneakyThrows
    public static String buildUrl(String url, Map<String, String> queryParams) {
        if (CollectionUtil.isEmpty(queryParams)) {
            return url;
        }
        boolean isFirst = true;
        StringBuilder builder = new StringBuilder(url);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            String key = entry.getKey();
            if (key != null && entry.getValue() != null) {
                if (isFirst) {
                    isFirst = false;
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                String value = URLEncoder.encode(queryParams.get(key), Constants.ENCODE)
                        .replaceAll("\\+", "%20");
                builder.append(key)
                        .append("=")
                        .append(value);
            }
        }
        return builder.toString();
    }

    /**
     * 包装请求头部
     *
     * @param headers 请求头
     * @return Headers对象
     */
    private static Headers buildHeaders(Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (MapUtil.isNotEmpty(headers)) {
            headers.forEach(headerBuilder::add);
        }
        return headerBuilder.build();
    }


    /**
     * 包装请求表单
     *
     * @param body 请求参数
     * @return FormBody对象
     */
    private static FormBody buildFormBody(Map<String, String> body) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (MapUtil.isNotEmpty(body)) {
            body.forEach(formBodyBuilder::add);
        }
        return formBodyBuilder.build();
    }

    /**
     * 包装请求json数据
     *
     * @param request 请求参数
     * @return RequestBody对象
     */
    private static RequestBody buildJsonBody(Object request) {
        MediaType contentType = MediaType.parse(HttpMediaType.APPLICATION_JSON);
        return RequestBody.create(JSONUtil.toJSONString(request), contentType);
    }

    /**
     * 包装返回结果，针对字符串
     *
     * @param result 请求结果
     * @return 字符串格式
     */
    private static String buildResponse(OkHttpResult result) {
        if (result.failed() && StringUtil.isNotBlank(result.getMessage())) {
            throw new RuntimeException(result.getMessage());
        }
        return byteToString(result.getBody());
    }

    /**
     * 包装返回结果，针对返回范型对象
     *
     * @param result 请求结果
     * @return 对象格式
     */
    private static <T> T buildResponse(OkHttpResult result, TypeReference<T> responseType) {
        if (result.failed() && StringUtil.isNotBlank(result.getMessage())) {
            throw new RuntimeException(result.getMessage());
        }
        return JSONUtil.parseObject(result.getBody(), responseType);
    }

    /**
     * 获取内容
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    private static String byteToString(byte[] bytes) {
        if (Objects.nonNull(bytes)) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return StringUtil.EMPTY;
    }

    /**
     * 公共请求调用
     *
     * @param request 请求对象
     * @return 请求结果
     */
    private static OkHttpResult commonRequest(Request request) {
        return OkHttpBuilder.syncResponse(request);
    }
}