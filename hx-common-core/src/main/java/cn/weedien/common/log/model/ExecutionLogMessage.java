package cn.weedien.common.log.model;

import cn.weedien.common.toolkit.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExecutionLogMessage extends BaseLogMessage {

    /**
     * 请求参数
     *
     * @apiNote 请求参数为Map或Object类型
     */
    private Object params;

    /**
     * 消耗时间
     */
    private String costTime;

    /**
     * 请求返回的状态码
     */
    private Integer code;

    /**
     * 请求异常信息
     */
    private String message;

    /**
     * 请求返回的结果
     */
    private Object result;

    public String startMessage() {
        Map<String, Object> logMessage = Map.of(
                "userId", userId,
                "uri", uri,
                "method", method,
                "clientIp", clientIp,
                "device", device,
                "methodInfo", methodInfo,
                "params", params
        );
        return JSONUtil.toJSONString(logMessage);
    }

    public String endMessage() {
        Map<String, Object> logMessage = Map.of(
                "code", code,
                "costTime", costTime,
                "result", result,
                "message", message
        );
        return JSONUtil.toJSONString(logMessage);

    }


    @Override
    public String toString() {
        return JSONUtil.toJSONString(this);
    }
}
