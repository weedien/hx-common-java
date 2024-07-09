package cn.weedien.common.toolkit;

import java.lang.reflect.Type;
import java.util.List;

/**
 * JSON 门面接口
 */
public interface JsonFacade {

    /**
     * To JSON string.
     *
     * @param object object
     * @return JSON string
     */
    String toJSONString(Object object);

    /**
     * Parse object.
     *
     * @param text  text
     * @param clazz class
     * @param <T>   type
     * @return object
     */
    <T> T parseObject(String text, Class<T> clazz);

    /**
     * Parse object.
     *
     * @param text      text
     * @param valueType valueType
     * @param <T>       type
     * @return object
     */
    <T> T parseObject(String text, Type valueType);

    /**
     * Parse array.
     *
     * @param text  text
     * @param clazz class
     * @param <T>   type
     * @return list
     */
    <T> List<T> parseArray(String text, Class<T> clazz);

    /**
     * Validate json.
     *
     * @param text text
     * @return true if json
     */
    boolean isJson(String text);


    <T> T parseObject(byte[] bytes, TypeReference<T> valueType);
}