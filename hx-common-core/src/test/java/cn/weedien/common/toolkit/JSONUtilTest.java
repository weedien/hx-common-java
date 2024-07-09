package cn.weedien.common.toolkit;


import cn.weedien.common.toolkit.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JSONUtilTest {

    public static final String PERSON_JSON = "{\"name\":\"John Doe\",\"age\":30}";

    public static final String PERSON_ARRAY_JSON = "[{\"name\":\"John Doe\",\"age\":30}, {\"name\":\"Jane Doe\",\"age\":25}]";

    @Test
    void toJSONString() {
    }

    @Test
    void parseObject() {
    }

    @Test
    void test_parseObjectWithType() {
        Type type = new TypeReference<List<Person>>() {
        }.getType();

        List<Person> result = JSONUtil.parseObject(PERSON_ARRAY_JSON, type);

        assertNotNull(result, "The result should not be null.");
        assertEquals("John Doe", result.getFirst().getName(), "The name of the first person should match.");
        assertEquals(30, result.getFirst().getAge(), "The age of the first person should match.");
    }

    @Test
    void test_parseArray() {
        String json = "[{\"name\":\"John Doe\",\"age\":30}, {\"name\":\"Jane Doe\",\"age\":25}]";

        List<Person> result = JSONUtil.parseArray(json, Person.class);

        assertNotNull(result, "The result should not be null.");
        assertEquals(2, result.size(), "The result list should have 2 elements.");
        assertNotNull(result.getFirst(), "Elements of the list should be of type Person.");
        assertEquals("John Doe", result.getFirst().getName(), "The name of the first person should match.");
        assertEquals(30, result.getFirst().getAge(), "The age of the first person should match.");
    }

    @Test
    void isJson() {
    }

    @Data
    private static class Person {
        private String name;
        private int age;
    }
}