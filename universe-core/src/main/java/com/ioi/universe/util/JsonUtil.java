package com.ioi.universe.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static com.fasterxml.jackson.databind.MapperFeature.USE_GETTERS_AS_SETTERS;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS;

public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER_INSTANCE;

    static {
        OBJECT_MAPPER_INSTANCE = JsonMapper.builder()
                .configure(USE_GETTERS_AS_SETTERS, false)
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(FAIL_ON_EMPTY_BEANS, false)
                .configure(READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                .configure(ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
                .configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(WRITE_DURATIONS_AS_TIMESTAMPS, false)
                .configure(FAIL_ON_IGNORED_PROPERTIES, true)
                .build();

        OBJECT_MAPPER_INSTANCE.setTypeFactory(
                OBJECT_MAPPER_INSTANCE
                        .getTypeFactory()
                        .withClassLoader(JsonUtil.class.getClassLoader()));
    }

    private JsonUtil() {
    }

    public static JsonNode toJsonNode(String json) {
        try {
            return OBJECT_MAPPER_INSTANCE.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("to jsonNode exception.", e);
        }
    }

    public static <T> T findVauleFirst(JsonNode jsonNode, String fieldName, Class<T> clazz) {
        JsonNode node = jsonNode.findValue(fieldName);
        if (node == null) return null;
        try {
            return OBJECT_MAPPER_INSTANCE.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("FindVauleFirst to object exception.", e);
        }
    }

    public static <T> T findVauleLast(JsonNode jsonNode, String fieldName, Class<T> clazz) {
        List<JsonNode> nodes = jsonNode.findValues(fieldName);
        if (nodes == null || nodes.size() <= 0) return null;
        try {
            return OBJECT_MAPPER_INSTANCE.treeToValue(nodes.get(nodes.size() - 1), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("FindVauleLast to object exception.", e);
        }
    }

    public static <T> List<T> findVaules(JsonNode jsonNode, String fieldName, Class<T> clazz) {
        List<JsonNode> nodes = jsonNode.findValues(fieldName);
        if (nodes == null || nodes.size() <= 0) return null;
        List<T> results = new ArrayList<>();
        nodes.forEach(node -> {
            try {
                results.add(OBJECT_MAPPER_INSTANCE.treeToValue(node, clazz));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("FindVaules to list exception.", e);
            }
        });
        return results;
    }

    public static <T> T parseObject(String json, Class<T> clazz) {

        try {
            return OBJECT_MAPPER_INSTANCE.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json to object exception.", e);
        }
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        try {
            CollectionType listType = OBJECT_MAPPER_INSTANCE.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return OBJECT_MAPPER_INSTANCE.readValue(json, listType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json to list exception.", e);
        }
    }

    public static Map<String, Object> toMap(String json) {
        return toMap(json, String.class, Object.class);
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return OBJECT_MAPPER_INSTANCE.readValue(json, new TypeReference<Map<K, V>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json to map exception.", e);
        }
    }

    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER_INSTANCE.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Object to json exception.", e);
        }
    }
}
