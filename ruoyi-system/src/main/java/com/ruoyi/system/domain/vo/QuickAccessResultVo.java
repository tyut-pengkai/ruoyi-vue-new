package com.ruoyi.system.domain.vo;

import java.util.HashMap;
import java.util.Map;

public class QuickAccessResultVo {
    private final Map<String, Object> data = new HashMap<>();
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
