package com.ruoyi.common.utils.desensitization.handler;


import com.ruoyi.common.utils.desensitization.DesensitizationUtil;

/**
 * 对象脱敏
 *
 * @author liangyuqi
 * @date 2022/4/29 14:08
 */
public class ObjectSensitiveHandler implements SensitiveHandler {
    @Override
    public <T> T handle(T src) {
        return DesensitizationUtil.desensitize(src);
    }
}
