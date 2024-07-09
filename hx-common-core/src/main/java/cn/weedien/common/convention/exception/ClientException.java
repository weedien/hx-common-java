package cn.weedien.common.convention.exception;

import cn.weedien.common.convention.result.ResultCode;
import cn.weedien.common.convention.result.IResultCode;

import java.io.Serial;

/**
 * 客户端异常
 * <p>请求参数异常
 */
public class ClientException extends AbstractException {

    @Serial
    private static final long serialVersionUID = -7389311943163781485L;

    public ClientException(String message) {
        this(message, null, ResultCode.CLIENT_ERROR);
    }

    public ClientException(IResultCode resultCode) {
        this(null, null, resultCode);
    }

    public ClientException(String message, IResultCode resultCode) {
        this(message, null, resultCode);
    }

    public ClientException(String message, Throwable throwable, IResultCode resultCode) {
        super(message, throwable, resultCode);
    }

    @Override
    public String toString() {
        return "ClientException{" + "code=" + code + ", message='" + message + "'}";
    }
}
