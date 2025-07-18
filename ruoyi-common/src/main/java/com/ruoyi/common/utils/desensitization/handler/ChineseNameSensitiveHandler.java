package com.ruoyi.common.utils.desensitization.handler;

import cn.hutool.core.util.StrUtil;

/**
 * 姓名脱敏
 *
 * @author liangyuqi
 * @date 2022/4/29 13:46
 */
public class ChineseNameSensitiveHandler implements SensitiveHandler {
    @Override
    public <T> T handle(T src) {
        if (src instanceof String) {
            int len = ((String) src).length();
            if (len > 1) {
                String pre = StrUtil.subPre((String) src, len - 2);
                String suf = StrUtil.subSuf((String) src, len - 1);
                src = (T) pre.concat("*").concat(suf);
            }
        }
        return src;
    }
}
