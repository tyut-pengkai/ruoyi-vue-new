package com.ruoyi.system.service;

import com.ruoyi.sale.mapper.SysSaleOrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@SpringBootTest
public class DashboardTest {

    @Resource
    private SysSaleOrderMapper sysSaleOrderMapper;

    @Test
    public void queryDashboardInfo() {
//        // 平台总交易额
//        BigDecimal totalFee = sysSaleOrderMapper.queryTotalFee();
//        System.out.println(totalFee);
//        // 今日成交
        LocalDate localDate = LocalDate.now();
//        String start = localDate.toString();
//        String end = localDate.plusDays(1).toString();
//        totalFee = sysSaleOrderMapper.queryTotalFeeBetween(start, end);
//        System.out.println(totalFee);
//        // 昨日成交
//        start = localDate.minusDays(1).toString();
//        end = localDate.toString();
//        totalFee = sysSaleOrderMapper.queryTotalFeeBetween(start, end);
//        System.out.println(totalFee);
//        // 近七日
//        start = localDate.minusDays(6).toString();
//        end = localDate.plusDays(1).toString();
//        totalFee = sysSaleOrderMapper.queryTotalFeeBetween(start, end);
//        System.out.println(totalFee);
//        // 本月成交
//        start = localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
//        end = localDate.with(TemporalAdjusters.firstDayOfNextMonth()).toString();
//        totalFee = sysSaleOrderMapper.queryTotalFeeBetween(start, end);
//        System.out.println(totalFee);
//        // APP
//        List<Map<String, Object>> mapList = sysSaleOrderMapper.queryAppTotalFee();
//        System.out.println(JSON.toJSONString(mapList));
        LocalDate monday = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        System.out.println(monday);
        System.out.println(sunday);

    }
}