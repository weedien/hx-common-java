package cn.weedien.common.idempotent.core.param;

import cn.weedien.common.idempotent.core.IdempotentExecuteHandler;

/**
 * 参数方式幂等实现接口
 * <p>基于userId、path、argsMD5生成幂等key
 */
public interface IdempotentParamService extends IdempotentExecuteHandler {
}
