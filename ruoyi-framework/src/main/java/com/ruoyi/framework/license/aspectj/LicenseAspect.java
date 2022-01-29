package com.ruoyi.framework.license.aspectj;

import com.ruoyi.common.exception.LicenseException;
import com.ruoyi.framework.license.LicenseVerify;
import com.ruoyi.framework.license.anno.CheckLicence;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;

@Order(0)
@ControllerAdvice
@Component
public class LicenseAspect implements RequestBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        //这里设置成false 它就不会再走这个类了
        if (methodParameter.getMethod().isAnnotationPresent(CheckLicence.class)) {
            //获取注解配置的包含和去除字段
            CheckLicence serializedField = methodParameter.getMethodAnnotation(CheckLicence.class);
            return serializedField.value();
        }
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        LicenseVerify licenseVerify = new LicenseVerify();
        //校验证书是否有效
        if (!licenseVerify.verify()) {
            throw new LicenseException("您的授权证书无效，请核查服务器是否取得授权或重新申请证书！");
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return o;
    }
}
