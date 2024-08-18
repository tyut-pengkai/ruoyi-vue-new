package com.ruoyi.quartz.task.impl;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BindType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.SysAppMapper;
import com.ruoyi.system.service.ISysAppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ClearUnbindlTimesTask {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private SysAppMapper appMapper;

    public void clearUnbindTimes() {
//        log.info("[设备解绑]开始重置解绑次数");
        SysApp appCondition = new SysApp();
        appCondition.setEnableUnbind(UserConstants.YES);
        appCondition.setStatus(UserConstants.NORMAL);
        // 获取试用周期不为0的软件
        List<SysApp> appList = appMapper.selectSysAppList(appCondition).stream().filter(item -> item.getUnbindCycle() != null && item.getUnbindCycle() != 0 && item.getBindType() != BindType.NONE).collect(Collectors.toList());
        if(appList.isEmpty()) {
            return;
        }
        Map<Long, SysApp> idToApp = appList.stream().collect(Collectors.toMap(SysApp::getAppId, item->item));
        List<SysAppUser> appUserList = appUserService.selectSysAppUserListByAppIdsAndNextEnableUnbindTimeBeforeNow(appList.stream().map(SysApp::getAppId).toArray(Long[]::new));
        List<SysAppUser> appUserListBatchList = new ArrayList<>();
        for (SysAppUser appUser : appUserList) {
            Date now = DateUtils.getNowDate();
            if (appUser.getNextEnableUnbindTime() == null || appUser.getNextEnableUnbindTime().before(now) || appUser.getNextEnableUnbindTime().equals(DateUtils.parseDate(UserConstants.MAX_DATE))) {
                SysApp app = idToApp.get(appUser.getAppId());
                Long unbindCycle = app.getUnbindCycle();
                SysAppUser updateUser = new SysAppUser();
                updateUser.setAppUserId(appUser.getAppUserId());
                updateUser.setUnbindTimes(app.getUnbindTimes());
                updateUser.setNextEnableUnbindTime(MyUtils.getNewExpiredTimeAdd(null, unbindCycle));
                appUserListBatchList.add(updateUser);
                log.info("[设备绑定]软件：{}-{} 软件用户：{} 解绑次数已重置", appUser.getAppId(), app.getAppName(), appUser.getApp().getAuthType() == AuthType.ACCOUNT ? appUser.getUser().getUserName() : appUser.getLoginCode());
            }
        }
        if(!appUserListBatchList.isEmpty()) {
            for (SysAppUser appUser : appUserListBatchList) {
                appUserService.updateSysAppUser(appUser);
            }
        }
//        log.info("[设备解绑]解绑次数重置完毕");
    }

}
