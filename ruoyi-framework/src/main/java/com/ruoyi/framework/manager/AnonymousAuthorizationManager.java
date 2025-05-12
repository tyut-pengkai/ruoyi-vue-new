package com.ruoyi.framework.manager;

import com.ruoyi.common.annotation.Anonymous;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Objects;
import java.util.function.Supplier;

@Component
public class AnonymousAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final RequestMappingHandlerMapping handlerMapping;

    public AnonymousAuthorizationManager(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        try {
            Object handler = handlerMapping.getHandler(context.getRequest()).getHandler();
            if (handler instanceof HandlerMethod handlerMethod) {
                if(Objects.nonNull( AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Anonymous.class))) {
                    return new AuthorizationDecision(true);
                }
                if (Objects.nonNull(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class))) {
                    return new AuthorizationDecision(true);
                }
            }
        } catch (Exception e) {
        }
        Authentication auth = authentication.get();
        if (Objects.isNull(auth) || !auth.isAuthenticated() || Objects.equals(auth.getName(), "anonymousUser")) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(true);
    }
}
