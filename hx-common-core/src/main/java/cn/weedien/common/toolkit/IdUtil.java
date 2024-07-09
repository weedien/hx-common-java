package cn.weedien.common.toolkit;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

import java.util.UUID;

/**
 * ID 生成工具类
 */
public class IdUtil {

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String simpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String simpleULID() {
        return UlidCreator.getUlid().toString();
    }

    public static String fastULID() {
        return Ulid.fast().toString();
    }

    public static long getSnowflakeNextId() {
        // TODO
        return 0L;
    }

    public static String getSnowflakeNextIdStr() {
        return String.valueOf(getSnowflakeNextId());
    }

}
