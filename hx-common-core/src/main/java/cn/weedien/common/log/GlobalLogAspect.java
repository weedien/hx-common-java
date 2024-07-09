package cn.weedien.common.log;

import cn.weedien.common.convention.exception.AbstractException;
import cn.weedien.common.convention.result.ResultCode;
import cn.weedien.common.log.model.ExecutionLogMessage;
import cn.weedien.common.toolkit.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class GlobalLogAspect extends BaseAspectSupport {

    /**
     * 切点，请求入口
     */
    @Pointcut("execution(public * cn.weedien.*.controller.*.*(..))")
    public void requestEntry() {
    }

    /**
     * 这个切面用于记录controller层的请求日志
     *
     * @param joinPoint 切点
     * @return Object
     * @throws Throwable 异常
     */
    @Around("requestEntry()")
    public Object requestAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        ExecutionLogMessage logMessage = new ExecutionLogMessage();

        try {
            // 请求参数 + 方法名称
            Method method = resolveMethod(joinPoint);
            logMessage.setParams(getParams(method, joinPoint.getArgs()));
            logMessage.setMethodInfo(joinPoint);
            logMessage.addRequestContext();

            log.info("request started: {}", logMessage.startMessage());
            Object result = joinPoint.proceed();
            logMessage.setCode(ResultCode.SUCCESS.code());
            logMessage.setMessage(ResultCode.SUCCESS.message());
            logMessage.setResult(result);
            return result;
        } catch (Throwable e) {
            if (e instanceof AbstractException ae) {
                logMessage.setCode(ae.getCode());
            }
            logMessage.setCode(ResultCode.UNKNOWN_ERROR.code());
            logMessage.setMessage(e.getMessage());
            throw e;
        } finally {
            // 正常运行/出现异常 都会打印日志
            long duration = System.nanoTime() - start;
            logMessage.setCostTime(DateUtil.formatDuration(duration));
            if (Objects.equals(logMessage.getCode(), ResultCode.SUCCESS.code())) {
                log.info("request completed: {}", logMessage.endMessage());
            } else {
                log.error("request failed: {}", logMessage.endMessage());
            }
        }
    }

    /**
     * 这个切面用于记录使用 @Log 注解的方法的日志
     * <p>
     * 记录方法参数、返回值、执行时间，如果withRequest为true，还会记录请求信息
     *
     * @param joinPoint 切点
     * @return Object
     * @throws Throwable 异常
     */
    @Around("@annotation(cn.weedien.common.log.Log)")
    public Object commonAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        ExecutionLogMessage logMessage = new ExecutionLogMessage();

        try {
            Method method = resolveMethod(joinPoint);
            Log logAnnotation = method.getAnnotation(Log.class);
            logMessage.setMethodInfo(joinPoint);

            if (logAnnotation.withContext()) {
                logMessage.addRequestContext();
            }

            log.info("execution started: {}", logMessage.startMessage());
            Object result = joinPoint.proceed();
            logMessage.setCode(ResultCode.SUCCESS.code());
            logMessage.setMessage(ResultCode.SUCCESS.message());
            logMessage.setResult(result);
            return result;
        } catch (Throwable e) {
            if (e instanceof AbstractException ae) {
                logMessage.setCode(ae.getCode());
            }
            logMessage.setCode(ResultCode.UNKNOWN_ERROR.code());
            logMessage.setMessage(e.getMessage());
            throw e;
        } finally {
            // 正常运行/出现异常 都会打印日志
            long duration = System.nanoTime() - start;
            logMessage.setCostTime(DateUtil.formatDuration(duration));
            if (Objects.equals(logMessage.getCode(), ResultCode.SUCCESS.code())) {
                log.info("execution completed: {}", logMessage.endMessage());
            } else {
                log.error("execution failed: {}", logMessage.endMessage());
            }
        }
    }

    /**
     * 获取方法参数
     *
     * @param method 方法
     * @param args   参数
     * @return 参数 Map / Object
     */
    private Object getParams(Method method, Object[] args) {
        Map<String, Object> paramMap = new HashMap<>();

        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter param = method.getParameters()[i];
            String key = param.isAnnotationPresent(RequestParam.class)
                    ? param.getAnnotation(RequestParam.class).value()
                    : param.getName();
            paramMap.put(key, args[i]);
        }

        return paramMap;
    }

}
