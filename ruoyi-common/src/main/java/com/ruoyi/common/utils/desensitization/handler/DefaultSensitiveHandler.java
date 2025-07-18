package com.ruoyi.common.utils.desensitization.handler;

import cn.hutool.core.util.StrUtil;

/**
 * 默认脱敏
 *
 * @author liangyuqi
 * @date 2022/4/29 14:08
 */
public class DefaultSensitiveHandler implements SensitiveHandler {
    @Override
    public <T> T handle(T src) {
        if (src instanceof String) {
            int len = ((String) src).length();
            if (len > 0) {
                src = (T) StrUtil.fillAfter("", '*', len);
            }
        }
        return src;
    }
}
