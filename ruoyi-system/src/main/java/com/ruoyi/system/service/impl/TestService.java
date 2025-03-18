package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.domain.TestDO;
import com.ruoyi.system.mapper.TestMapper;
import com.ruoyi.system.service.ITestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-03-18 16:08
 */
@Slf4j
@Service
public class TestService implements ITestService {
    @Autowired
    private TestMapper testMapper;

    @Transactional
    @Override
    public void insertTest() {
        TestDO testDO = new TestDO();
        testDO.setDescription("测试插入" + DateUtil.now());
        testDO.setVersion(0L);
        testMapper.insert(testDO);
        log.info("插入:{}", testDO);
    }

    @Transactional
    @Override
    public void updateTest() {
        TestDO testDO = new TestDO();
        testDO.setId(1L);
        testDO.setDescription("测试修改" + DateUtil.now());
        testDO.setVersion(1L);
        log.info("修改:{}", testMapper.updateById(testDO));
    }

    @Override
    public void listTest() {
        List<TestDO> testDOList = testMapper.selectList(Wrappers.lambdaQuery(TestDO.class).eq(TestDO::getId, 1));
        log.info("查询:{}", testDOList);
    }
}
