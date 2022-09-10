package com.ruoyi.api.v1.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class BaseLocator implements BeanFactoryAware {

    private static BeanFactory factory = null;

    private static BaseLocator baselocator = null;

    public static BaseLocator getInstance() {
        if (baselocator == null)
            baselocator = (BaseLocator) factory.getBean("baseLocator");
        return baselocator;
    }

    @Override
    public void setBeanFactory(BeanFactory f) throws BeansException {
        factory = f;
    }

    public BeanFactory getFactory() {
        return factory;
    }
}