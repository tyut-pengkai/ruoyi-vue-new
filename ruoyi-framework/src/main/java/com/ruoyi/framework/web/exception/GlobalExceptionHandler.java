package com.ruoyi.framework.web.exception;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.DemoModeException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.html.EscapeUtil;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.system.domain.SysOperLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        SysOperLog optLog = createOptLog(e, request);
        log.error("请求地址'{}',权限校验失败'{}'", optLog.getOperUrl(), e.getMessage());
        sendExceptionMsg(optLog);
        return AjaxResult.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request)
    {
        SysOperLog optLog = createOptLog(e, request);
        log.error("请求地址'{}',不支持'{}'请求", optLog.getOperUrl(), e.getMethod());
        sendExceptionMsg(optLog);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        SysOperLog optLog = createOptLog(e, request);
        log.error("请求地址'{}',发生业务异常'{}'", optLog.getOperUrl(), e.getMessage());
        sendExceptionMsg(optLog);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     * 断言异常
     * {@link cn.hutool.core.lang.Assert}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public AjaxResult illegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        SysOperLog optLog = createOptLog(e, request);
        log.error("请求地址'{}',发生校验异常'{}'", optLog.getOperUrl(), e.getMessage());
        sendExceptionMsg(optLog);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        SysOperLog optLog = createOptLog(e, request);
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", optLog.getOperUrl(), e);
        sendExceptionMsg(optLog);
        return AjaxResult.error(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request)
    {
        SysOperLog optLog = createOptLog(e, request);
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value))
        {
            value = EscapeUtil.clean(value);
        }
        log.error("请求参数类型不匹配'{}',发生系统异常.", optLog.getOperUrl(), e);
        sendExceptionMsg(optLog);
        return AjaxResult.error(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), value));
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        SysOperLog optLog = createOptLog(e, request);
        log.error("请求地址'{}',发生未知异常.", optLog.getOperUrl(), e);
        sendExceptionMsg(optLog);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        SysOperLog optLog = createOptLog(e, request);
        log.error("请求地址'{}',发生系统异常.", optLog.getOperUrl(), e);
        sendExceptionMsg(optLog);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e, HttpServletRequest request)
    {
        SysOperLog optLog = createOptLog(e, request);
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        sendExceptionMsg(optLog);
        return AjaxResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request)
    {
        SysOperLog optLog = createOptLog(e, request);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("请求地址'{}',发生校验异常: {}", optLog.getOperUrl(), message);
        sendExceptionMsg(optLog);
        return AjaxResult.error(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        return AjaxResult.error("演示模式，不允许操作");
    }

    /**
     * 发送异常消息
     *
     * @param optLog
     */
    private void sendExceptionMsg(SysOperLog optLog) {
        log.info("【异常请求日志】{}", optLog);
        SpringUtils.getBean(FsNotice.class).sendException2MonitorChat(optLog);
    }

    private <T extends Exception> SysOperLog createOptLog(T e, HttpServletRequest request) {
        SysOperLog syslog = new SysOperLog();
        syslog.setTitle(e.getClass().getName());
        syslog.setRequestMethod(request.getMethod());
        syslog.setOperName(SecurityUtils.getUsernameSafe());
        syslog.setOperUrl(request.getRequestURI());
        syslog.setOperIp(IpUtils.getIpAddr(request));
        syslog.setOperParam(JSONUtil.toJsonStr(getParamsMap(request)));
        syslog.setStatus(1);
        syslog.setErrorMsg(e.getMessage());
        syslog.setOperTime(new Date());
        return syslog;
    }

    /**
     * 获取参数
     *
     * @param request
     * @return
     */
    private Map<String, String> getParamsMap(HttpServletRequest request) {
        Map<String, String[]> requestParams = request.getParameterMap();
        if (MapUtil.isEmpty(requestParams)) {
            return MapUtil.empty();
        }
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            StringBuilder valueStrBuilder = new StringBuilder();
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    valueStrBuilder.append(values[i]);
                    if (i != values.length - 1) {
                        valueStrBuilder.append(",");
                    }
                }
            }
            params.put(name, valueStrBuilder.toString());
        }
        return params;
    }
}
