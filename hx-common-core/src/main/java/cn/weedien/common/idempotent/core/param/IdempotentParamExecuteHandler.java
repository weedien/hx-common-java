package cn.weedien.common.idempotent.core.param;

import cn.weedien.common.auth.UserContext;
import cn.weedien.common.idempotent.core.AbstractIdempotentExecuteHandler;
import cn.weedien.common.idempotent.core.IdempotentContext;
import cn.weedien.common.idempotent.core.IdempotentParamWrapper;
import cn.weedien.common.toolkit.HashUtil;
import cn.weedien.common.toolkit.HttpContextUtil;
import cn.weedien.common.toolkit.StringUtil;
import cn.weedien.common.convention.exception.ClientException;
import cn.weedien.common.convention.result.ResultCode;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * 基于方法参数验证请求幂等性
 */
@RequiredArgsConstructor
public final class IdempotentParamExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentParamService {

    private final RedissonClient redissonClient;

    private final static String LOCK = "lock:param:restapi";

    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        String lockKey = String.format("idempotent:path:%s:userid:%s:md5:%s", getServletPath(), getCurrentUserId(), calcArgsMD5(joinPoint));
        return IdempotentParamWrapper.builder().lockKey(lockKey).joinPoint(joinPoint).build();
    }

    /**
     * @return 获取当前线程上下文 ServletPath
     */
    private String getServletPath() {
        return HttpContextUtil.getRequest().getServletPath();
    }

    /**
     * @return 当前操作用户 ID
     */
    private String getCurrentUserId() {
        String userId = UserContext.getUserId();
        if (StringUtil.isBlank(userId)) {
            throw new ClientException("用户ID获取失败，请登录");
        }
        return userId;
    }

    /**
     * @return joinPoint md5
     */
    private String calcArgsMD5(ProceedingJoinPoint joinPoint) {
        return HashUtil.getMD5Hash(JSON.toJSONBytes(joinPoint.getArgs()));
    }

    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        String lockKey = wrapper.getLockKey();
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            throw new ClientException(wrapper.getIdempotent().message(), ResultCode.IDEMPOTENT_PARAM_ERROR);
        }
        IdempotentContext.put(LOCK, lock);
    }

    @Override
    public void postProcessing() {
        RLock lock = null;
        try {
            lock = (RLock) IdempotentContext.getKey(LOCK);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    @Override
    public void exceptionProcessing() {
        RLock lock = null;
        try {
            lock = (RLock) IdempotentContext.getKey(LOCK);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }
}
