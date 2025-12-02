package com.ruoyi.framework.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
public class VirtualThreadConfig {

    /**
     * 配置虚拟线程池：JDK 21 新增 Executors.newVirtualThreadPerTaskExecutor()
     * 特性：1个任务对应1个虚拟线程，无需手动设置核心线程数/最大线程数
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        // 1. 创建自定义虚拟线程工厂并指定名称前缀
        ThreadFactory virtualThreadFactory = Thread.ofVirtual()
                .name("virtual-api-", 1) // 前缀为"virtual-api-"，编号从1开始
                .factory();

        // 2. 使用上面创建的线程工厂初始化执行器
        return protocolHandler -> protocolHandler.setExecutor(
                Executors.newThreadPerTaskExecutor(virtualThreadFactory)
        );
    }

}
