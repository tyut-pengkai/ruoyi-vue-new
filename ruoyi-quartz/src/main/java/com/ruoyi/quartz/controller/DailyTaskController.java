package com.ruoyi.quartz.controller;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.task.XktTask;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/daily-task")
public class DailyTaskController extends BaseController {

    final XktTask task;

    @PostMapping("/season")
    public R season(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.seasonTag();
        return R.ok();
    }


    @PostMapping("/daily-sale")
    public R dailySale(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailySale();
        return R.ok();
    }

    @PostMapping("/prod")
    public R dailySaleProd(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailySaleProduct();
        return R.ok();
    }

    @PostMapping("/cus")
    public R dailySaleCus(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailySaleCustomer();
        return R.ok();
    }

    @PostMapping("/cate-sort")
    public R dailyCateSort(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyCategorySort();
        return R.ok();
    }

    @PostMapping("/store-tag")
    public R dailyStoreTag(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyStoreTag();
        return R.ok();
    }

    @PostMapping("/prod-tag")
    public R dailyProdTag(SysJob sysJob) throws IOException {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyProdTag();
        return R.ok();
    }

    @PostMapping("/prod-weight")
    public R dailyProdWeight(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyProdWeight();
        return R.ok();
    }

    @PostMapping("/store-weight")
    public R dailyStoreWeight(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyStoreWeight();
        return R.ok();
    }

    @PostMapping("/advert-round")
    public R dailyRound(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyAdvertRound();
        return R.ok();
    }

    @PostMapping("/advert-round-filter")
    public R dailyRoundFilterTime(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.saveAdvertDeadlineToRedis();
        return R.ok();
    }

    @PostMapping("/store-redis")
    public R saveStoreToRedis(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.saveStoreToRedis();
        return R.ok();
    }

    @PostMapping("/symbol-redis")
    public R saveSymbolToRedis(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.saveSymbolToRedis();
        return R.ok();
    }

    @PostMapping("/user-search-history")
    public R dailyUserSearchHistory(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyUpdateUserSearchHistory();
        return R.ok();
    }

    @PostMapping("/search-hot")
    public R dailySearchHot(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyUpdateSearchHotToRedis();
        return R.ok();
    }

    @PostMapping("/publish-prod")
    public R hourPublicStoreProduct(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.hourPublicStoreProduct();
        return R.ok();
    }

    @PostMapping("/store-memeber")
    public R expireStoreMember(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.autoCloseExpireStoreMember();
        return R.ok();
    }

    @PostMapping("/redis/store-memeber")
    public R saveStoreMemberToRedis(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.saveStoreMemberToRedis();
        return R.ok();
    }

    @PostMapping("/daily-prod-top-sale")
    public R dailyProdTopSale(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyProdTopSale();
        return R.ok();
    }

    @PostMapping("/daily-prod-statistics")
    public R dailyProductStatistics(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyProductStatistics();
        return R.ok();
    }

    @PostMapping("/reset-voucher-sequence")
    public R resetVoucherSequence(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.resetVoucherSequence();
        return R.ok();
    }

    @PostMapping("/reset-advert-data")
    public R clearAndUpdateAdvertShowData(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.clearAndUpdateAdvertShowData();
        return R.ok();
    }

    @PostMapping("/store-visit")
    public R updateStoreVisitCount(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.updateStoreVisitCount();
        return R.ok();
    }

    @PostMapping("/user-brows-history")
    public R dailyUpdateUserBrowsingHistory(SysJob sysJob) {
        // 用户是否为超级管理员
        if (!SecurityUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员，无权限操作!", HttpStatus.ERROR);
        }
        task.dailyUpdateUserBrowsingHistory();
        return R.ok();
    }


}
