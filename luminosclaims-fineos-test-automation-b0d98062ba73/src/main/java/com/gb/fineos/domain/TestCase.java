package com.gb.fineos.domain;

import java.util.Map;

public class TestCase {
    private final String name;
    private final String desc;
    private final Map<String, String> data;

    public TestCase(final String name, final String desc, final Map<String, String> data) {
        this.name = name;
        this.desc = desc;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Map<String, String> getData() {
        return data;
    }
}
