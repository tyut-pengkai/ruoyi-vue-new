package com.ruoyi.quickAccessApk;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysAppVersionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class TemplateTest {

    @Resource
    private ISysAppVersionService appVersionService;

    @Test
    public void test() {
        AjaxResult quickAccessTemplateList = appVersionService.getQuickAccessTemplateList();
        System.out.println(JSON.toJSONString(quickAccessTemplateList));
    }

}