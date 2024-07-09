package cn.weedien.common.toolkit;

import org.slf4j.MDC;

/**
 * traceId工具类
 * <p>将所有对MDC的操作都集中封装在这里
 */
public class TraceUtil {
    public static final String TRACE_ID = "traceId";
    /**
     * 当traceId为空时，显示的traceId。随意。
     */
    private static final String DEFAULT_TRACE_ID = "0";

    /**
     * 设置traceId
     */
    public static void setTraceId(String traceId) {
        // 防止重复设置traceId
        if (StringUtil.hasText(getTraceId())) {
            return;
        }
        // 如果参数为空，则设置默认traceId
        traceId = StringUtil.isBlank(traceId) ? DEFAULT_TRACE_ID : traceId;
        // 将traceId放到MDC中
        MDC.put(TRACE_ID, traceId);
    }

    /**
     * 获取traceId
     */
    public static String getTraceId() {
        // 获取
        String traceId = MDC.get(TRACE_ID);
        // 如果traceId为空，则返回默认值
        return StringUtil.isBlank(traceId) ? DEFAULT_TRACE_ID : traceId;
    }

    /**
     * 移除traceId
     */
    public static void removeTraceId() {
        MDC.remove(TRACE_ID);
    }

    /**
     * 判断traceId为默认值
     */
    public static Boolean isDefaultTraceId(String traceId) {
        return DEFAULT_TRACE_ID.equals(traceId);
    }

    /**
     * 生成traceId
     */
    public static String genTraceId() {
        return IdUtil.simpleULID();
    }
}

