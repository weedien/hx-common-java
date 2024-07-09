package cn.weedien.common.toolkit;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * JSON工具类 - 门面模式
 */
public class JSONUtil {

    public static final JsonFacade JSON_FACADE = new JacksonHandler();

    /**
     * Object转换为JSON字符串
     *
     * @param object object
     * @return JSON字符串
     */
    public static String toJSONString(Object object) {
        if (object == null) {
            return null;
        }
        return JSON_FACADE.toJSONString(object);
    }

    /**
     * JSON字符串转换为Object
     *
     * @param text  JSOn字符串
     * @param clazz class
     * @param <T>   type
     * @return object
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StringUtil.isBlank(text)) {
            return null;
        }
        return JSON_FACADE.parseObject(text, clazz);
    }

    /**
     * JSON字符串转换为Object
     *
     * @param text      text
     * @param valueType valueType
     * @param <T>       type
     * @return object
     */
    public static <T> T parseObject(String text, Type valueType) {
        if (StringUtil.isBlank(text)) {
            return null;
        }
        return JSON_FACADE.parseObject(text, valueType);
    }

    /**
     * JSON字符串转换为List
     *
     * @param text  JSON字符串
     * @param clazz class
     * @param <T>   type
     * @return list
     */
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StringUtil.isBlank(text)) {
            return Collections.emptyList();
        }
        return JSON_FACADE.parseArray(text, clazz);
    }

    /**
     * 判断是否为JSON字符串
     *
     * @param json JSON字符串
     * @return true if json
     */
    public static boolean isJson(String json) {
        if (StringUtil.isBlank(json)) {
            return false;
        }
        return JSON_FACADE.isJson(json);
    }

    /**
     * 将字节流转对象，支持范型类
     *
     * @param value
     * @return
     */
    public static <T> T parseObject(byte[] value, TypeReference<T> referenceType) {
        if (Objects.isNull(value)) {
            return null;
        }
        return JSON_FACADE.parseObject(value, referenceType);
    }
}
