package com.ruoyi.sale.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.sale.service.ISysSaleShopService;
import com.ruoyi.system.mapper.SysCardMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
class PaymentServiceTest {

    @Resource
    private ISysSaleShopService saleShopService;

    @Resource
    private SysCardMapper cardMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void test() {
        Map<Long, Long> all = saleShopService.getSaleableCardSizeAll();
        System.out.println(JSON.toJSONString(all));
//        List<Map<Long, Long>> mapList = cardMapper.countSysCardAll(new SysCard());
//        System.out.println(JSON.toJSONString(mapList));
    }

}
