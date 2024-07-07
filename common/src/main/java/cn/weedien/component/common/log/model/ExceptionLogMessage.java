package cn.weedien.component.common.log.model;

import cn.weedien.component.common.toolkit.JSONUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ExceptionLogMessage extends BaseLogMessage {

    private Integer code;

    private String message;

    @Override
    public String toString() {
        return JSONUtil.toJSONString(this);
    }
}
