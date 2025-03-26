package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.TestVO;
import com.ruoyi.xkt.dto.TestDTO;
import com.ruoyi.xkt.service.ITestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-03-26 14:40
 */
@Api(tags = "鞋库通测试")
@Slf4j
@RestController
@RequestMapping("/xkt/test")
public class XktTestController {

    @Autowired
    private ITestService iTestService;

    @ApiOperation("执行测试")
    @GetMapping("/execute")
    public R<List<TestVO>> execute() {
        List<TestDTO> dtoList = iTestService.listTest();
        return R.ok(BeanUtil.copyToList(dtoList, TestVO.class));
    }
}
