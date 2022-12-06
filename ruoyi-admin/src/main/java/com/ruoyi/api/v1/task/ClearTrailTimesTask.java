package com.ruoyi.api.v1.task;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppTrialUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.SysAppMapper;
import com.ruoyi.system.service.ISysAppTrialUserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
@EnableAsync
public class ClearTrailTimesTask {

    @Resource
    private ISysAppTrialUserService appTrialUserService;
    @Resource
    private SysAppMapper appMapper;

    @Async
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void clearTrailTimes() {
        SysApp appCondition = new SysApp();
        appCondition.setEnableTrial(UserConstants.YES);
        appCondition.setEnableTrialByTimes(UserConstants.YES);
        appCondition.setStatus(UserConstants.NORMAL);
        List<SysApp> appList = appMapper.selectSysAppList(appCondition);
        for (SysApp app : appList) {
            SysAppTrialUser userCondition = new SysAppTrialUser();
            userCondition.setAppId(app.getAppId());
            List<SysAppTrialUser> trialUserList = appTrialUserService.selectSysAppTrialUserList(userCondition);
            for (SysAppTrialUser appTrialUser : trialUserList) {
                Long trialCycle = app.getTrialCycle();
                if (trialCycle != null && trialCycle != 0) {
                    Date now = DateUtils.getNowDate();
                    if (appTrialUser.getNextEnableTime().before(now) || appTrialUser.getNextEnableTime().equals(DateUtils.parseDate(UserConstants.MAX_DATE))) {
                        appTrialUser.setLoginTimes(0L);
                        appTrialUser.setNextEnableTime(MyUtils.getNewExpiredTimeAdd(null, trialCycle));
                        appTrialUserService.updateSysAppTrialUser(appTrialUser);
                    }
                }
            }
        }
    }

}
