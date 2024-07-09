package cn.weedien.common.idempotent.core.token;

import cn.weedien.common.cache.DistributedCache;
import cn.weedien.common.idempotent.config.IdempotentProperties;
import cn.weedien.common.idempotent.core.AbstractIdempotentExecuteHandler;
import cn.weedien.common.idempotent.core.IdempotentParamWrapper;
import cn.weedien.common.toolkit.HttpContextUtil;
import cn.weedien.common.toolkit.IdUtil;
import cn.weedien.common.toolkit.StringUtil;
import cn.weedien.common.convention.exception.ClientException;
import cn.weedien.common.convention.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Optional;

/**
 * 基于 Token 验证请求幂等性, 通常应用于 RestAPI 方法
 */
@RequiredArgsConstructor
public final class IdempotentTokenExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentTokenService {

    private final DistributedCache distributedCache;
    private final IdempotentProperties idempotentProperties;

    private static final String TOKEN_KEY = "token";
    private static final String TOKEN_PREFIX_KEY = "idempotent:token:";
    private static final long TOKEN_EXPIRED_TIME = 6000;

    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        return new IdempotentParamWrapper();
    }

    /**
     * 基于UUID生成Token
     *
     * @return Token
     */
    @Override
    public String createToken() {
        String token = Optional.ofNullable(StringUtil.emptyToNull(idempotentProperties.getPrefix())).orElse(TOKEN_PREFIX_KEY) + IdUtil.simpleUUID();
        distributedCache.put(token, StringUtil.EMPTY, Optional.ofNullable(idempotentProperties.getTimeout()).orElse(TOKEN_EXPIRED_TIME));
        return token;
    }

    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        HttpServletRequest request = HttpContextUtil.getRequest();
        String token = request.getHeader(TOKEN_KEY);
        if (StringUtil.isBlank(token)) {
            token = request.getParameter(TOKEN_KEY);
            if (StringUtil.isBlank(token)) {
                throw new ClientException(ResultCode.IDEMPOTENT_TOKEN_NULL_ERROR);
            }
        }
        Boolean tokenDelFlag = distributedCache.delete(token);
        if (!tokenDelFlag) {
            String errMsg = StringUtil.isNotBlank(wrapper.getIdempotent().message())
                    ? wrapper.getIdempotent().message()
                    : ResultCode.IDEMPOTENT_TOKEN_DELETE_ERROR.message();
            throw new ClientException(errMsg, ResultCode.IDEMPOTENT_TOKEN_DELETE_ERROR);
        }
    }
}
