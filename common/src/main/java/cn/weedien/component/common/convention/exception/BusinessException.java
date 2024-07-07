package cn.weedien.component.common.convention.exception;

import cn.weedien.component.common.convention.result.IResultCode;
import cn.weedien.component.common.convention.result.ResultCode;

import java.io.Serial;

/**
 * 业务异常
 */
public class BusinessException extends AbstractException {

    @Serial
    private static final long serialVersionUID = 7614479358404005432L;

    public BusinessException(String message) {
        this(message, null, ResultCode.BUSINESS_ERROR);
    }

    public BusinessException(IResultCode resultCode) {
        this(null, resultCode);
    }

    public BusinessException(String message, IResultCode resultCode) {
        this(message, null, resultCode);
    }

    public BusinessException(String message, Throwable throwable, IResultCode resultCode) {
        super(message, throwable, resultCode);
    }

    @Override
    public String toString() {
        return "BusinessException{" + "code=" + code + ", message='" + message + "'}";
    }
}
