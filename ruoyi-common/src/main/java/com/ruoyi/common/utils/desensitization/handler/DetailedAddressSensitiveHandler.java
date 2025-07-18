package com.ruoyi.common.utils.desensitization.handler;

import cn.hutool.core.util.StrUtil;

/**
 * 详细地址脱敏
 *
 * @author liangyuqi
 * @date 2022/4/29 14:08
 */
public class DetailedAddressSensitiveHandler implements SensitiveHandler {
    @Override
    public <T> T handle(T src) {
        if (src instanceof String) {
            int len = ((String) src).length();
            if (len > 9) {
                String pre = StrUtil.subPre((String) src, 9);
                src = (T) pre.concat(StrUtil.fillAfter("", '*', len - 9));
            }
        }
        return src;
    }
}
