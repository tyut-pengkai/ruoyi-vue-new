package com.ruoyi.common.exception;

import com.ruoyi.common.enums.ErrorCode;

/**
 * 业务异常
 *
 * @author ruoyi
 */
public final class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ApiException() {
    }

    public ApiException(String detailMessage) {
        this(ErrorCode.ERROR_OTHER_FAULTS, detailMessage);
    }

    public ApiException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public ApiException(ErrorCode errorCode, String detailMessage) {
        this.message = errorCode.getMsg();
        this.code = errorCode.getCode();
        this.detailMessage = detailMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public ApiException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ApiException setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getCode() {
        return code;
    }
}