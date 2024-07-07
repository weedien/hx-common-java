package cn.weedien.component.common.toolkit;

import com.alibaba.fastjson2.JSON;

import java.lang.reflect.Type;
import java.util.List;

/**
 * FastJson Handler
 */
public class FastJsonHandler implements JsonFacade {
    @Override
    public String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

    @Override
    public <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    @Override
    public <T> T parseObject(String text, Type valueType) {
        return JSON.parseObject(text, valueType);
    }

    @Override
    public <T> T parseObject(byte[] bytes, TypeReference<T> valueType) {
        return JSON.parseObject(bytes, valueType);
    }

    @Override
    public <T> List<T> parseArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    @Override
    public boolean isJson(String text) {
        return JSON.isValid(text);
    }

}
