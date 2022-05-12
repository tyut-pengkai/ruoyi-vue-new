package com.ruoyi.framework.hy.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class HyDuser implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private HyTuser daemonThread;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //root application context 没有parent
        ApplicationContext context = event.getApplicationContext().getParent();
        if (context == null) {
            daemonThread.forceLogout();
        }
    }
}

