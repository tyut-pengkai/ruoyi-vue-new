package com.ruoyi.quartz.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.task.XktTask;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/daily-sale")
public class DailySaleController extends BaseController {

    final XktTask task;

    @PostMapping("")
    public R dailySale(SysJob sysJob) {
        task.dailySale();
        return R.ok();
    }

    @PostMapping("/prod")
    public R dailySaleProd(SysJob sysJob) {
        task.dailySaleProduct();
        return R.ok();
    }

    @PostMapping("/cus")
    public R dailySaleCus(SysJob sysJob) {
        task.dailySaleCustomer();
        return R.ok();
    }

    @PostMapping("/cate-sort")
    public R dailyCateSort(SysJob sysJob) {
        task.categorySort();
        return R.ok();
    }

    @PostMapping("/store-tag")
    public R dailyStoreTag(SysJob sysJob) {
        task.dailyStoreTag();
        return R.ok();
    }

    @PostMapping("/prod-tag")
    public R dailyProdTag(SysJob sysJob) {
        task.dailyProdTag();
        return R.ok();
    }

    @PostMapping("/prod-weight")
    public R dailyProdWeight(SysJob sysJob) {
        task.dailyProdWeight();
        return R.ok();
    }

}
