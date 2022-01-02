package com.ruoyi.api.v1.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ZwGu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/*@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)*/
public @interface Encrypt {

    /**
     * 入参是否解密，默认解密
     */
    boolean in() default true;

    /**
     * 出参是否加密，默认加密
     */
    boolean out() default true;
}
