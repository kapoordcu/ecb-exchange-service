package com.capital.scalable.domain;

import java.util.HashMap;
import java.util.Map;

public class LogMessage {
    private final Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public LogMessage with(String key, Object value) {
        put(key, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();
        data.entrySet()
                .forEach(obj -> content.append(obj.getKey() + "," + obj.getValue()).append("\t"));
        return content.toString();
    }
}
