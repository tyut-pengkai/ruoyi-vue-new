package com.ruoyi.framework.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 演示模式拦截器
 *
 * @author ruoyi
 */
@Component
public class DemoInterceptor implements HandlerInterceptor {

    @Value("${server.demo.enable}")
    private boolean demoEnabled;
    @Resource
    private DenyOperationListConfig denyOperation;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (demoEnabled) {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                Log annotation = method.getAnnotation(Log.class);
                if (annotation != null && denyOperation.getDenyOperation().contains(annotation.businessType().name())) {
                    AjaxResult ajaxResult = AjaxResult.error("演示模式，当前操作被禁止！");
                    ServletUtils.renderString(response, JSONObject.toJSONString(ajaxResult));
                    return false;
                }
                return true;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Configuration
    @ConfigurationProperties(prefix = "server.demo")
    public class DenyOperationListConfig {
        private List<String> denyOperation;

        public List<String> getDenyOperation() {
            return denyOperation;
        }

        public void setDenyOperation(List<String> denyOperation) {
            this.denyOperation = denyOperation;
        }
    }
}
