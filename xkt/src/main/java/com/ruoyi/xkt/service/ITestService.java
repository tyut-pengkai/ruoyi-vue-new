package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.TestDTO;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-03-18 16:07
 */
public interface ITestService {

    void insertTest(TestDTO testDTO);

    void updateTest(TestDTO testDTO);

    List<TestDTO> listTest();
}
