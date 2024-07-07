package cn.weedien.component.common.log.model;

import cn.weedien.component.common.toolkit.HttpContextUtil;
import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;

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


    public void addRequestContext() {
        this.clientIp = HttpContextUtil.getIpAddr();
        this.device = HttpContextUtil.getDevice();
        this.method = HttpContextUtil.getRequestMethod();
        this.uri = HttpContextUtil.getRequestURI();
    }

    public void setMethodInfo(ProceedingJoinPoint joinPoint) {
        this.methodInfo = joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName() + ":" +
                joinPoint.getSourceLocation().getLine();
    }
}
