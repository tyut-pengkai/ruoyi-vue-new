package com.ruoyi.common.utils.desensitization.handler;

import cn.hutool.core.util.StrUtil;

/**
 * 电话号码脱敏
 *
 * @author liangyuqi
 * @date 2022/4/29 14:08
 */
public class PhoneNoSensitiveHandler implements SensitiveHandler {
    @Override
    public <T> T handle(T src) {
        if (src instanceof String) {
            int len = ((String) src).length();
            if (len > 7) {
                String pre = StrUtil.subPre((String) src, 3);
                String suf = StrUtil.subSuf((String) src, len - 4);
                src = (T) pre.concat(StrUtil.fillAfter("", '*', len - 7)).concat(suf);
            }
        }
        return src;
    }
}
