package cn.weedien.component.common.database.keygen;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.apache.shardingsphere.infra.algorithm.core.context.AlgorithmSQLContext;
import org.apache.shardingsphere.infra.algorithm.keygen.core.KeyGenerateAlgorithm;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public final class CustomSnowflakeKeyGenerator implements KeyGenerateAlgorithm {

    public CustomSnowflakeKeyGenerator() {
    }

    public CustomSnowflakeKeyGenerator(short workerId) {
        IdGeneratorOptions options = new IdGeneratorOptions(workerId);
        YitIdHelper.setIdGenerator(options);
    }

    public void init(Properties props) {
        short workerId = Short.parseShort(props.getProperty("worker-id"));
        IdGeneratorOptions options = new IdGeneratorOptions(workerId);
        YitIdHelper.setIdGenerator(options);
    }

    public String getType() {
        return "CUSTOM_SNOWFLAKE";
    }

    @Override
    public Collection<Long> generateKeys(AlgorithmSQLContext algorithmSQLContext, int i) {
        return Collections.singletonList(YitIdHelper.nextId());
    }
}
