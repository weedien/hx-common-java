package cn.weedien.component.common.filter;

import cn.weedien.component.common.toolkit.StringUtil;
import cn.weedien.component.common.toolkit.TraceUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static cn.weedien.component.common.toolkit.TraceUtil.TRACE_ID;

/**
 * traceId过滤器
 */
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            // traceId初始化
            initTraceId((HttpServletRequest) servletRequest);
            // 执行后续过滤器
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // 移除traceId
            TraceUtil.removeTraceId();
        }

    }

    /**
     * traceId初始化
     */
    private void initTraceId(HttpServletRequest request) {
        // 尝试获取http请求中的traceId
        String traceId = request.getHeader(TRACE_ID);
        if (StringUtil.isBlank(traceId)) {
            traceId = request.getParameter(TRACE_ID);
        }
        // 如果当前traceId为空或者为默认traceId，则生成新的traceId
        if (StringUtil.isBlank(traceId) || TraceUtil.isDefaultTraceId(traceId)) {
            traceId = TraceUtil.genTraceId();
        }
        // 设置traceId
        TraceUtil.setTraceId(traceId);
    }
}
