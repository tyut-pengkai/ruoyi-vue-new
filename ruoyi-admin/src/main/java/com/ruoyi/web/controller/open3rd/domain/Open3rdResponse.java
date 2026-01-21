package com.ruoyi.web.controller.open3rd.domain;

public class Open3rdResponse<T>
{
    private int code;
    private String message;
    private T data;

    public Open3rdResponse()
    {
    }

    public Open3rdResponse(int code, String message, T data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Open3rdResponse<T> success(T data)
    {
        return new Open3rdResponse<>(0, "success", data);
    }

    public static <T> Open3rdResponse<T> error(int code, String message)
    {
        return new Open3rdResponse<>(code, message, null);
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}
