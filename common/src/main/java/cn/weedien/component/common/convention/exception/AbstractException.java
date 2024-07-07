package cn.weedien.component.common.convention.exception;

import cn.weedien.component.common.convention.result.IResultCode;
import lombok.Getter;

import java.io.Serial;

@Getter
public abstract class AbstractException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8787374018818605333L;

    protected final Integer code;

    protected final String message;

    public AbstractException(String message, Throwable throwable, IResultCode resultCode) {
        super(message, throwable);
        this.code = resultCode.code();
        this.message = message != null ? message : resultCode.message();
    }
}
