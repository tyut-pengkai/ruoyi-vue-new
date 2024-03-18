package com.ruoyi.api.v1.task;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppTrialUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.SysAppMapper;
import com.ruoyi.system.service.ISysAppTrialUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@EnableAsync
@Slf4j
public class ClearTrailTimesTask {

    @Resource
    private ISysAppTrialUserService appTrialUserService;
    @Resource
    private SysAppMapper appMapper;

    @Async
    @Scheduled(cron = "0 0/1 * * * ?")
    public void clearTrailTimes() {
//        log.info("[软件试用]开始重置试用周期");
        SysApp appCondition = new SysApp();
        appCondition.setEnableTrial(UserConstants.YES);
        appCondition.setEnableTrialByTimes(UserConstants.YES);
        appCondition.setStatus(UserConstants.NORMAL);
        // 获取试用周期不为0的软件
        List<SysApp> appList = appMapper.selectSysAppList(appCondition).stream().filter(item -> item.getTrialCycle() != null && item.getTrialCycle() != 0).collect(Collectors.toList());
        if(appList.isEmpty()) {
            return;
        }
        Map<Long, Long> idToTrialCycle = appList.stream().collect(Collectors.toMap(SysApp::getAppId, SysApp::getTrialCycle));
        List<SysAppTrialUser> trialUserList = appTrialUserService.selectSysAppTrialUserListByAppIdsAndNextEnableTimeBeforeNow(appList.stream().map(SysApp::getAppId).toArray(Long[]::new));
        List<SysAppTrialUser> trialUserListBatchList = new ArrayList<>();
        for (SysAppTrialUser appTrialUser : trialUserList) {
            Date now = DateUtils.getNowDate();
            if (appTrialUser.getNextEnableTime().before(now) || appTrialUser.getNextEnableTime().equals(DateUtils.parseDate(UserConstants.MAX_DATE))) {
                Long trialCycle = idToTrialCycle.get(appTrialUser.getAppId());
                appTrialUser.setLoginTimes(0L);
                appTrialUser.setNextEnableTime(MyUtils.getNewExpiredTimeAdd(null, trialCycle));
                trialUserListBatchList.add(appTrialUser);
                log.info("[软件试用]试用软件ID：{} 试用用户：{} 试用周期已重置", appTrialUser.getAppId(), appTrialUser.getUserName());
            }
        }
        if(!trialUserListBatchList.isEmpty()) {
            appTrialUserService.updateBatchById(trialUserListBatchList);
        }
//        log.info("[软件试用]试用周期重置完毕");
    }

}
