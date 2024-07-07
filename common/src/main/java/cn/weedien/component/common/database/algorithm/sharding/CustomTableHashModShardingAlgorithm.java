package cn.weedien.component.common.database.algorithm.sharding;

import org.apache.shardingsphere.infra.algorithm.core.exception.AlgorithmInitializationException;
import org.apache.shardingsphere.infra.exception.core.ShardingSpherePreconditions;
import org.apache.shardingsphere.sharding.algorithm.sharding.ShardingAutoTableAlgorithmUtils;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

public final class CustomTableHashModShardingAlgorithm implements StandardShardingAlgorithm<Comparable<?>> {

    private static final String SHARDING_COUNT_KEY = "sharding-count";

    private int shardingCount;

    @Override
    public void init(final Properties props) {
        shardingCount = getShardingCount(props);
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Comparable<?>> shardingValue) {
        String suffix = String.valueOf(hashShardingValue(shardingValue.getValue()) % (shardingCount - 1));
        return ShardingAutoTableAlgorithmUtils.findMatchedTargetName(availableTargetNames, suffix, shardingValue.getDataNodeInfo()).orElse(null);
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Comparable<?>> shardingValue) {
        return availableTargetNames;
    }

    private int getShardingCount(final Properties props) {
        ShardingSpherePreconditions.checkState(props.containsKey(SHARDING_COUNT_KEY),
                () -> new AlgorithmInitializationException(this, "Sharding count cannot be null."));
        return Integer.parseInt(props.getProperty(SHARDING_COUNT_KEY));
    }

    private long hashShardingValue(final Object shardingValue) {
        return Math.abs((long) shardingValue.hashCode());
    }

    @Override
    public String getType() {
        return "CLASS_BASED";
    }
}
