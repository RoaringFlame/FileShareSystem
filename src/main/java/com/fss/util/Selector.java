package com.fss.util;


public class Selector {
    private String key;
    private String value;
    private String MiddleKey;

    public Selector(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Selector(String key, String value, String middleKey) {
        this.key = key;
        this.value = value;
        MiddleKey = middleKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMiddleKey() {
        return MiddleKey;
    }

    public void setMiddleKey(String middleKey) {
        MiddleKey = middleKey;
    }
}
