package com.ruoyi.common.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 * 
 * @author duanyangyang
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission
{
    String[] value() default {};
}
