package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.xkt.domain.Test;
import com.ruoyi.xkt.dto.TestDTO;
import com.ruoyi.xkt.mapper.TestMapper;
import com.ruoyi.xkt.service.ITestService;
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
public class TestServiceImpl implements ITestService {
    @Autowired
    private TestMapper testMapper;

    @Transactional
    @Override
    public void insertTest(TestDTO testDTO) {
        Test test = new Test();
        test.setDescription(testDTO.getDescription());
        test.setVersion(0L);
        testMapper.insert(test);
        log.info("插入:{}", test);
    }

    @Transactional
    @Override
    public void updateTest(TestDTO testDTO) {
        Test test = new Test();
        test.setId(testDTO.getId());
        test.setDescription(testDTO.getDescription());
        test.setVersion(testDTO.getVersion());
        log.info("修改:{}", testMapper.updateById(test));
    }

    @Override
    public List<TestDTO> listTest() {
        List<Test> testList = testMapper.selectList(Wrappers.lambdaQuery(Test.class).eq(Test::getId, 1));
        log.info("查询:{}", testList);
        return BeanUtil.copyToList(testList, TestDTO.class);
    }
}
