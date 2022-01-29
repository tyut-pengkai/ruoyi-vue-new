package com.ruoyi.framework.license.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ZwGu
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LicenceCheck {

    /**
     * 入参是否解密，默认解密
     */
    boolean value() default true;

}
