package com.ruoyi.web.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author liangyq
 * @date 2025-11-11
 */
//@WebFilter(urlPatterns = "/rest/v1/common/html/content/*")
@Order()
public class HtmlFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}