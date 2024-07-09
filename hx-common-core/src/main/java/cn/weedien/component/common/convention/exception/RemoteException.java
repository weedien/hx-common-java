package cn.weedien.component.common.convention.exception;

import cn.weedien.component.common.convention.result.IResultCode;
import cn.weedien.component.common.convention.result.ResultCode;

import java.io.Serial;

public class RemoteException extends AbstractException {

    @Serial
    private static final long serialVersionUID = 8148562412991953779L;

    public RemoteException(String message) {
        this(message, null, ResultCode.REMOTE_ERROR);
    }

    public RemoteException(IResultCode resultCode) {
        this(null, resultCode);
    }

    public RemoteException(String message, IResultCode resultCode) {
        this(message, null, resultCode);
    }

    public RemoteException(String message, Throwable throwable, IResultCode resultCode) {
        super(message, throwable, resultCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" + "code=" + code + ", message='" + message + "'}";
    }

}
