package com.ruoyi.common.utils.desensitization.handler;

/**
 * 脱敏处理类
 *
 * @author liangyuqi
 * @date 2022/4/29 11:22
 */
public interface SensitiveHandler {
    /**
     * 对数据的值进行脱敏处理
     *
     * @param src src
     * @return 脱敏后的数据
     */
    <T> T handle(T src);
}
