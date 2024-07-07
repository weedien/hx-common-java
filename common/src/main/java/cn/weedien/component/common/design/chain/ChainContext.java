package cn.weedien.component.common.design.chain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽象责任链上下文
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class ChainContext<T> implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    private final Map<String, List<ChainHandler>> chainHandlerContainer = new HashMap<>();

    public ChainContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void handle(String mark, T param) {
        List<ChainHandler> handlers = chainHandlerContainer.get(mark);
        if (CollectionUtils.isEmpty(handlers)) {
            throw new RuntimeException(String.format("[%s] Chain of Responsibility ID is undefined.", mark));
        }
        handlers.forEach(handler -> handler.handle(param));
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, ChainHandler> chainFilterMap = applicationContext.getBeansOfType(ChainHandler.class);
        chainFilterMap.forEach((beanName, bean) -> {
            List<ChainHandler> chainHandlers = chainHandlerContainer.getOrDefault(bean.mark(), new ArrayList<>());
            chainHandlers.add(bean);
            // 排序
            List<ChainHandler> actualAbstractChainHandlers = chainHandlers.stream()
                    .sorted(Comparator.comparing(Ordered::getOrder))
                    .collect(Collectors.toList());
            chainHandlerContainer.put(bean.mark(), actualAbstractChainHandlers);
        });
    }
}
