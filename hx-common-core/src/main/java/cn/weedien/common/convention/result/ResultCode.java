package cn.weedien.common.convention.result;

public enum ResultCode implements IResultCode {

    SUCCESS(200, "请求成功"),

    SERVLET_ERROR(400, "请求异常"),

    METHOD_ARGUMENT_NOT_VALID(400, "不合法的请求参数"),

    BIND_ERROR(400, "参数绑定异常"),

    BUSINESS_ERROR(400, "业务异常"),

    CLIENT_ERROR(400, "客户端异常"),

    REMOTE_ERROR(400, "调用第三方服务出错"),

    UNKNOWN_ERROR(500, "未定义异常"),

    IDEMPOTENT_SPEL_ERROR(501, "幂等SpEL异常"),

    IDEMPOTENT_PARAM_ERROR(505, "幂等Param异常"),

    IDEMPOTENT_TOKEN_DELETE_ERROR(506, "幂等Token删除失败"),

    IDEMPOTENT_TOKEN_NULL_ERROR(507, "幂等Token为空"),
    ;

    private final Integer code;

    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
