package com.ruoyi.system.service;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.DateUtils;
import org.junit.Test;

import java.util.TimeZone;

public class DateConvertTest {


    @Test
    public void test() {
        // 设置全局时区为“Asia/Shanghai”
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println(DateUtils.parseDate(UserConstants.MAX_DATE));
    }


}
