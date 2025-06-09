package com.ruoyi.rs.core.exception;

public class OhBizException extends RuntimeException{

    public OhBizException(String s) {
        super(s);
    }

    public OhBizException(String message, Throwable cause) {
        super(message, cause);
    }

}
