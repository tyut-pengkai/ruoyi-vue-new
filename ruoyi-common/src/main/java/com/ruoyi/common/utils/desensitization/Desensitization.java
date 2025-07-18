package com.ruoyi.common.utils.desensitization;

import java.lang.annotation.*;

/**
 * 脱敏注解
 *
 * @author liangyuqi
 * @date 2022/4/29 11:22
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitization {
    /**
     * 类型
     */
    SensitiveTypeEnum value();
}
