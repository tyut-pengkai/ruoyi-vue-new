package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.service.IStatisticsService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/statis")
public class StatisticsController  extends BaseController {

    @Autowired
    private IStatisticsService statisticsService;

    @Anonymous
    @GetMapping("")
    public AjaxResult statisticsUpdate(@RequestParam("type") String type) {
        if (type == null || type.isEmpty()) {
            return AjaxResult.error("类型不能为空");
        }

        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime startOfDay = LocalDate.now(zone).atStartOfDay(zone);
        long timestamp = startOfDay.toEpochSecond();

        if (type.equals("web")) {
            statisticsService.increaseType(type, timestamp);
            return AjaxResult.success();
        } else if (type.equals("wxapp")) {
            statisticsService.increaseType(type, timestamp);
            return AjaxResult.success();
        } else {
            return AjaxResult.error("类型不存在");
        }
    }

    @Anonymous
    @GetMapping("/count")
    public AjaxResult getStatisticsCount(@RequestParam("type") String type) {
        if (type == null || type.isEmpty()) {
            return AjaxResult.error("类型不能为空");
        }

        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime startOfDay = LocalDate.now(zone).atStartOfDay(zone);
        long timestamp = startOfDay.toEpochSecond();

        if (type.equals("web") || type.equals("wxapp")) {
            int count;
            try {
                count = statisticsService.getTypeCount(type, timestamp);
            } catch (Exception e) {
                count = 0;
            }
            return AjaxResult.success(count);
        } else {
            return AjaxResult.error("类型不存在");
        }
    }

    @Anonymous
    @GetMapping("/history")
    public AjaxResult getStatisticsHistory(@RequestParam("type") String type,
                                           @RequestParam("start_ts") long start_ts,
                                           @RequestParam("end_ts") long end_ts) {
        if (type == null || type.isEmpty()) {
            return AjaxResult.error("类型不能为空");
        }

        if (start_ts > end_ts || end_ts < 0) {
            return AjaxResult.error("查询时间参数错误");
        }

        if (type.equals("web")) {
            return AjaxResult.success(statisticsService.getTypeHistoryCount(type, start_ts, end_ts));
        } else if (type.equals("wxapp")) {
            return AjaxResult.success(statisticsService.getTypeHistoryCount(type, start_ts, end_ts));
        } else {
            return AjaxResult.error("类型不存在");
        }
    }
}
