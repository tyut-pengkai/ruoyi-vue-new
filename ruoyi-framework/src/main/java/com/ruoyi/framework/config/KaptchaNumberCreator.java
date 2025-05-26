package com.ruoyi.framework.config;

import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.text.impl.DefaultTextCreator;

/**
 * 验证码文本生成器
 *
 * @author liangyq
 */
public class KaptchaNumberCreator extends DefaultTextCreator {

    @Override
    public String getText() {
        return RandomUtil.randomNumbers(5);
    }

}