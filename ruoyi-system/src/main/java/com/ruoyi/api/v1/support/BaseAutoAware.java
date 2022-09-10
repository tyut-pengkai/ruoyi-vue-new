package com.ruoyi.api.v1.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public class BaseAutoAware {

    public BaseAutoAware() {
        ((AutowireCapableBeanFactory) retrieveBeanFactory())
                .autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    }

    private BeanFactory retrieveBeanFactory() {
        BaseLocator bl = new BaseLocator();
//      BaseLocator bl = BaseLocator.getInstance();
        return bl.getFactory();
    }
}