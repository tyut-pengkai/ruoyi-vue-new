package com.ruoyi.framework.license.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.license.LicenseCheckListener;
import com.ruoyi.framework.license.LicenseVerify;
import com.ruoyi.framework.license.anno.LicenceCheck;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 证书检查拦截器
 *
 * @author ruoyi
 */
@Component
public class LicenseInterceptor implements HandlerInterceptor {

    @Resource
    private LicenseCheckListener licenseCheckListener;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LicenceCheck annotation = method.getAnnotation(LicenceCheck.class);
            if (annotation != null && annotation.value()) {
                // 重载授权证书
                licenseCheckListener.loadLicense();
                //校验证书是否有效
                LicenseVerify licenseVerify = new LicenseVerify();
                if (!licenseVerify.verify()) {
                    AjaxResult ajaxResult = AjaxResult.error("您的授权证书无效或已过期，请检查服务器是否取得授权或重新申请证书！您的设备码为（用于激活授权，请自行复制）：" + Constants.SERVER_SN);
                    ServletUtils.renderString(response, JSONObject.toJSONString(ajaxResult));
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}
