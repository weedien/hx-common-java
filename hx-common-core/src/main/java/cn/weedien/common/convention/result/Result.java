package cn.weedien.common.convention.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一返回对象
 *
 * @param <T> 返回数据类型
 * @author weedien
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6164405622792230441L;

    private Integer code;

    private String message;

    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.code(), ResultCode.SUCCESS.message(), data);
    }

    public static <T> Result<T> failed() {
        return new Result<>(ResultCode.UNKNOWN_ERROR.code(), ResultCode.UNKNOWN_ERROR.message(), null);
    }

    public static <T> Result<T> failure(IResultCode errorResult) {
        return new Result<>(errorResult.code(), errorResult.message(), null);
    }

    public static <T> Result<T> failure(Integer code, String message) {
        return new Result<>(code, message, null);
    }

}
