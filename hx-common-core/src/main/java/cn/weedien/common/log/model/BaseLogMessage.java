package cn.weedien.common.log.model;

import cn.weedien.common.toolkit.HttpContextUtil;
import cn.weedien.common.toolkit.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 基础日志信息，包含的是用户请求上下文信息
 */
@Getter
public class BaseLogMessage {

    /**
     * 用户名
     */
    protected String userId;

    /**
     * URI
     */
    protected String uri;

    /**
     * 请求类型
     */
    protected String method;

    /**
     * 请求端IP地址
     */
    protected String clientIp;

    /**
     * 请求设备信息，PC/Android/iOS
     */
    protected String device;

    /**
     * 方法信息
     */
    protected String methodInfo;

    /**
     * 请求参数
     */
    protected String reqParams;

    /**
     * 请求体
     */
    protected String reqBody;


    public void addRequestContext() {
        HttpServletRequest request = HttpContextUtil.getRequest();
        this.clientIp = HttpContextUtil.getIpAddr();
        this.device = request.getHeader("User-Agent");
        this.method = request.getMethod();
        this.uri = request.getRequestURI();
        this.userId = request.getHeader("userId");
        if (this.userId == null) {
            this.userId = request.getParameter("userId");
        }
        this.reqParams = JSONUtil.toJSONString(request.getParameterMap());
        try {
            this.reqBody = getRequestBody(request);
        } catch (IOException ignored) {
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    public void setMethodInfo(ProceedingJoinPoint joinPoint) {
        this.methodInfo = joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName() + ":" +
                joinPoint.getSourceLocation().getLine();
    }
}
