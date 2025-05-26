package com.ruoyi.framework.sms.ali.entity;

import lombok.Data;

@Data
public class Result {

    private String code;

    private String message;

    public Result() {

    }

    public Result(String message) {
        this.message = message;
    }

    public boolean isOk() {
        return "OK".equals(code);
    }
}
