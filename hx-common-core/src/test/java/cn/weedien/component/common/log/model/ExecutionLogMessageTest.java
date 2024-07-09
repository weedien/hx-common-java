package cn.weedien.component.common.log.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ExecutionLogMessageTest {

    @Test
    public void testJson() {
        ExecutionLogMessage message = new ExecutionLogMessage();
        message.setCostTime("100ms");
        message.setCode(200);
        message.setMessage("success");
        message.setResult("Hello World!");

        log.info("json: {}", message);
    }

}