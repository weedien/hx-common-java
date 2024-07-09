package cn.weedien.component.common;

import cn.weedien.component.common.log.model.ExceptionLogMessage;
import cn.weedien.component.common.convention.exception.AbstractException;
import cn.weedien.component.common.convention.exception.BusinessException;
import cn.weedien.component.common.convention.exception.ClientException;
import cn.weedien.component.common.convention.exception.RemoteException;
import cn.weedien.component.common.convention.result.Result;
import cn.weedien.component.common.convention.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication
@ConditionalOnMissingBean(GlobalExceptionHandler.class)
public class GlobalExceptionHandler {

    /**
     * Controller上一层相关异常处理
     *
     * @param e 异常
     * @return ErrorResponse
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public ResponseEntity<Result<Void>> handleServletException(Exception e) {
        logException(e, ResultCode.SERVLET_ERROR.code());
        return ResponseEntity.badRequest().body(Result.failure(ResultCode.SERVLET_ERROR));
    }

    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> handleBindException(BindException e) {
        logException(e, ResultCode.BIND_ERROR.code());
        return ResponseEntity.badRequest().body(Result.failure(ResultCode.BIND_ERROR));
    }

    /**
     * 参数校验异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logException(e, ResultCode.METHOD_ARGUMENT_NOT_VALID.code());
        return ResponseEntity.badRequest().body(Result.failure(ResultCode.METHOD_ARGUMENT_NOT_VALID));
    }

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 异常结果s
     */
    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<Result<Void>> handleAbstractException(AbstractException e) {
        return switch (e) {
            case ClientException ignored1 -> {
                logException(e, ResultCode.CLIENT_ERROR.code());
                yield ResponseEntity.badRequest().body(Result.failure(ResultCode.CLIENT_ERROR));
            }
            case BusinessException ignored2 -> {
                logException(e, ResultCode.BUSINESS_ERROR.code());
                yield ResponseEntity.badRequest().body(Result.failure(ResultCode.BUSINESS_ERROR));

            }
            case RemoteException ignored3 -> {
                logException(e, ResultCode.REMOTE_ERROR.code());
                yield ResponseEntity.badRequest().body(Result.failure(ResultCode.REMOTE_ERROR));
            }
            default -> {
                logException(e, ResultCode.UNKNOWN_ERROR.code());
                yield ResponseEntity.internalServerError().body(Result.failure(ResultCode.UNKNOWN_ERROR));
            }
        };
    }

    /**
     * 未知异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Result<Void>> handleThrowable(Throwable e) {
        logException(e, ResultCode.UNKNOWN_ERROR.code());
        return ResponseEntity.internalServerError().body(Result.failure(ResultCode.UNKNOWN_ERROR));
    }

    private void logException(Throwable e, int code) {
        ExceptionLogMessage logMessage = new ExceptionLogMessage();
        logMessage.setCode(code);
        logMessage.setMessage(e.getMessage());
        logMessage.addRequestContext();

        log.error("error occur: {}", logMessage);
    }
}
