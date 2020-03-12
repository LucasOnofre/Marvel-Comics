package com.onoffrice.marvel_comics.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringFormatUtil {

    public static StringFormatUtil from(String format) {
        return new StringFormatUtil(format);
    }

    private final String format;
    private final Map<String, Object> tags = new LinkedHashMap<String, Object>();

    private StringFormatUtil(String format) {
        this.format = format;
    }

    public StringFormatUtil with(String key, Object value) {
        tags.put("\\{" + key + "\\}", value);
        return this;
    }

    public String format() {
        String formatted = format;
        for (Map.Entry<String, Object> tag : tags.entrySet()) {
            // bottleneck, creating temporary String objects!
            formatted = formatted.replaceAll(tag.getKey(), tag.getValue().toString());
        }
        return formatted;
    }
}
