package cn.weedien.component.common.idempotent.core.token;

import cn.weedien.component.common.idempotent.core.IdempotentExecuteHandler;

/**
 * Token 实现幂等接口
 * <p>以用户请求时携带的token作为幂等验证
 */
public interface IdempotentTokenService extends IdempotentExecuteHandler {

    /**
     * 创建幂等验证Token
     */
    String createToken();
}
