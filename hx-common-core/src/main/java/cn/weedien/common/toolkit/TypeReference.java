package cn.weedien.common.toolkit;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> implements Type {

    protected final Type type;

    protected TypeReference() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        } else {
            this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }
    }

    public static <T> TypeReference<T> of(Class<T> clazz) {
        return new TypeReference<T>() {
        };
    }

    public Type getType() {
        return this.type;
    }

    public String toString() {
        return this.type.toString();
    }
}
