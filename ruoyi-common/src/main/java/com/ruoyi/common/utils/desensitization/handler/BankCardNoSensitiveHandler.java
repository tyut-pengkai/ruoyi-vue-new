package com.ruoyi.common.utils.desensitization.handler;

import cn.hutool.core.util.StrUtil;

/**
 * 银行卡号脱敏
 *
 * @author liangyuqi
 * @date 2022/4/29 14:08
 */
public class BankCardNoSensitiveHandler implements SensitiveHandler {
    @Override
    public <T> T handle(T src) {
        if (src instanceof String) {
            int len = ((String) src).length();
            if (len > 8) {
                String pre = StrUtil.subPre((String) src, 4);
                String suf = StrUtil.subSuf((String) src, len - 4);
                src = (T) pre.concat(StrUtil.fillAfter("", '*', len - 8)).concat(suf);
            }
        }
        return src;
    }
}
