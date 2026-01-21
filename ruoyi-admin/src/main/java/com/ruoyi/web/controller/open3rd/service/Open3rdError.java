package com.ruoyi.web.controller.open3rd.service;

public enum Open3rdError
{
    INVALID_TOKEN(10001, "凭证 X-Open3rd-Token 无效或用户不存在"),
    INVALID_PARAM(10002, "请求参数错误"),
    INTERNAL_ERROR(10003, "系统内部错误"),
    FILE_NOT_FOUND(10004, "文件不存在"),
    PERMISSION_DENIED(10005, "用户操作文件权限不足"),
    SIGNATURE_FAILED(10006, "签名校验失败");

    private final int code;
    private final String message;

    Open3rdError(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
