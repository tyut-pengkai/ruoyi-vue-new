package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysAppVersion;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ISysAppVersionServiceTest {

    @Resource
    private ISysAppVersionService versionService;

    @Test
    public void selectLatestVersionByAppId() {
        SysAppVersion version = versionService.selectLatestVersionByAppId(7L);
        System.out.println(version);
    }
}