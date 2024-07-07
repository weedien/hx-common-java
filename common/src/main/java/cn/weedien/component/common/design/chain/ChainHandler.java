package cn.weedien.component.common.design.chain;

import org.springframework.core.Ordered;

public interface ChainHandler<T> extends Ordered {

    /**
     * 执行责任链逻辑
     *
     * @param param 参数
     */
    void handle(T param);

    /**
     * @return 责任链标识
     */
    String mark();
}
