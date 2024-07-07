package cn.weedien.component.common.idempotent.core.token;

import cn.weedien.component.common.convention.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于 Token 验证请求幂等性控制器
 * <p>这个controller是专为幂等组件准备的，不参与鉴权，
 * token幂等处理基于用户请求时携带的token，只要有token就行
 */
@RestController
@RequiredArgsConstructor
public class IdempotentTokenController {

    private final IdempotentTokenService idempotentTokenService;

    /**
     * 请求申请Token
     */
    @GetMapping("/token")
    public Result<String> createToken() {
        return Result.success(idempotentTokenService.createToken());
    }
}
