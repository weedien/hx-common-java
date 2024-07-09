package cn.weedien.component.common.design.strategy;

import cn.weedien.component.common.base.ApplicationContextHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@SuppressWarnings("rawtypes")
public final class StrategySelector implements CommandLineRunner {

    /**
     * 执行策略集合
     */
    private final Map<String, ExecuteStrategy> executeStrategyMap = new HashMap<>();

    /**
     * 根据 mark 查询具体策略
     *
     * @param mark          策略标识
     * @param predicateFlag 匹配范解析标识
     * @return 实际执行策略
     */
    public ExecuteStrategy choose(String mark, Boolean predicateFlag) {
        if (predicateFlag != null && predicateFlag) {
            return executeStrategyMap.values().stream()
                    .filter(each -> StringUtils.hasText(each.patternMatchMark()))
                    .filter(each -> Pattern.compile(each.patternMatchMark()).matcher(mark).matches())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("策略未定义"));
        }
        return Optional.ofNullable(executeStrategyMap.get(mark))
                .orElseThrow(() -> new RuntimeException(String.format("[%s] 策略未定义", mark)));
    }

    /**
     * 根据 mark 查询具体策略并执行
     *
     * @param mark         策略标识
     * @param requestParam 执行策略入参
     * @param <REQUEST>    执行策略入参范型
     */
    public <REQUEST> void chooseAndExecute(String mark, REQUEST requestParam) {
        ExecuteStrategy executeStrategy = choose(mark, null);
        executeStrategy.execute(requestParam);
    }

    /**
     * 根据 mark 查询具体策略并执行
     *
     * @param mark          策略标识
     * @param requestParam  执行策略入参
     * @param predicateFlag 匹配范解析标识
     * @param <REQUEST>     执行策略入参范型
     */
    public <REQUEST> void chooseAndExecute(String mark, REQUEST requestParam, Boolean predicateFlag) {
        ExecuteStrategy executeStrategy = choose(mark, predicateFlag);
        executeStrategy.execute(requestParam);
    }

    /**
     * 根据 mark 查询具体策略并执行，带返回结果
     *
     * @param mark         策略标识
     * @param requestParam 执行策略入参
     * @param <REQUEST>    执行策略入参范型
     * @param <RESPONSE>   执行策略出参范型
     * @return 执行策略后返回值
     */
    public <REQUEST, RESPONSE> RESPONSE chooseAndExecuteResp(String mark, REQUEST requestParam) {
        ExecuteStrategy executeStrategy = choose(mark, null);
        return (RESPONSE) executeStrategy.executeResp(requestParam);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, ExecuteStrategy> actual = ApplicationContextHolder.getBeansOfType(ExecuteStrategy.class);
        actual.forEach((beanName, bean) -> {
            ExecuteStrategy beanExist = executeStrategyMap.get(bean.mark());
            if (beanExist != null) {
                throw new RuntimeException(String.format("[%s] Duplicate execution policy", bean.mark()));
            }
            executeStrategyMap.put(bean.mark(), bean);
        });
    }
}
