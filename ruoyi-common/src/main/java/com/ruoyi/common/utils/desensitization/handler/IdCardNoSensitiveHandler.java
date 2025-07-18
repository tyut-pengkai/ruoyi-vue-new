package com.ruoyi.common.utils.desensitization.handler;

import cn.hutool.core.util.StrUtil;

/**
 * 身份证脱敏
 *
 * @author liangyuqi
 * @date 2022/4/29 14:08
 */
public class IdCardNoSensitiveHandler implements SensitiveHandler {
    @Override
    public <T> T handle(T src) {
        if (src instanceof String) {
            int len = ((String) src).length();
            if (len > 4) {
                String pre = StrUtil.subPre((String) src, 2);
                String suf = StrUtil.subSuf((String) src, len - 2);
                src = (T) pre.concat(StrUtil.fillAfter("", '*', len - 4)).concat(suf);
            }
        }
        return src;
    }
}
