package cn.weedien.common.toolkit.okhttp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OkHttpResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * http级，状态标识码
     */
    private Integer code;

    /**
     * http级，错误信息
     */
    private String message;

    /**
     * http级，返回头部
     */
    Map<String, List<String>> headers;

    /**
     * http级，返回body
     */
    private byte[] body;

    public boolean failed() {
        return !success;
    }

    public OkHttpResult() {
    }

    public OkHttpResult(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}