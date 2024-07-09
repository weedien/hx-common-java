package cn.weedien.common.idempotent.core;

import cn.weedien.common.base.ApplicationContextHolder;
import cn.weedien.common.idempotent.core.param.IdempotentParamService;
import cn.weedien.common.idempotent.core.spel.IdempotentSpELByMQExecuteHandler;
import cn.weedien.common.idempotent.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import cn.weedien.common.idempotent.core.token.IdempotentTokenService;
import cn.weedien.common.idempotent.enums.IdempotentSceneEnum;
import cn.weedien.common.idempotent.enums.IdempotentTypeEnum;

/**
 * 幂等执行处理器工厂
 */
public final class IdempotentExecuteHandlerFactory {

    /**
     * 获取幂等执行处理器
     *
     * @param scene 指定幂等验证场景类型
     * @param type  指定幂等处理类型
     * @return 幂等执行处理器
     */
    public static IdempotentExecuteHandler getInstance(IdempotentSceneEnum scene, IdempotentTypeEnum type) {
        IdempotentExecuteHandler result = null;
        switch (scene) {
            case RESTAPI -> {
                switch (type) {
                    case PARAM -> result = ApplicationContextHolder.getBean(IdempotentParamService.class);
                    case TOKEN -> result = ApplicationContextHolder.getBean(IdempotentTokenService.class);
                    case SPEL -> result = ApplicationContextHolder.getBean(IdempotentSpELByRestAPIExecuteHandler.class);
                    default -> {
                    }
                }
            }
            case MQ -> result = ApplicationContextHolder.getBean(IdempotentSpELByMQExecuteHandler.class);
            default -> {
            }
        }
        return result;
    }
}
