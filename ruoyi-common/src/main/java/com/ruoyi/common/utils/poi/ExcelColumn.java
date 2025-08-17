package com.ruoyi.common.utils.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LH
 * @date 2021-04-23 13:30:54
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelColumn {
    /**
     * 表头
     */
    String head() default "";

    /**
     * 排序
     */
    int index() default -1;

}
