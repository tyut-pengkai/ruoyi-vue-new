package com.ruoyi.task;

import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysAppUserCount;
import com.ruoyi.system.mapper.DashboardAppViewMapper;
import com.ruoyi.system.mapper.SysAppMapper;
import com.ruoyi.system.mapper.SysAppUserMapper;
import com.ruoyi.system.service.ISysAppUserCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@EnableAsync
@Slf4j
public class RecordAppUserNumTask {
    @Resource
    private SysAppMapper appMapper;
    @Resource
    private SysAppUserMapper appUserMapper;
    @Resource
    private DashboardAppViewMapper dashboardAppViewMapper;
    @Resource
    private ISysAppUserCountService appUserCountService;

    @Async
    @Scheduled(cron = "0 1 0 * * ?") // 每日00点01分执行
    // @Scheduled(cron = "0 0/1 * * * ?") // 每分钟执行
    public void recordAppUserNum() {
        log.info("[用户统计]开始统计用户数据");
        // 全部
        Map<Long, List<SysAppUser>> allMap = appUserMapper.selectSysAppUserList(new SysAppUser()).stream().collect(Collectors.groupingBy(SysAppUser::getAppId));
        // 登录数量
        Map<Long, Long> loginMap = new HashMap<>();
        List<Map<String, Object>> loginMapList = dashboardAppViewMapper.queryAppUserLogin();
        for (Map<String, Object> map : loginMapList) {
            loginMap.put(Long.parseLong(map.get("app_id").toString()), Long.parseLong(map.get("total_user").toString()));
        }
        // vip数量
        Map<Long, Long> vipMap = new HashMap<>();
        List<Map<String, Object>> vipMapList = dashboardAppViewMapper.queryAppUserVip();
        for (Map<String, Object> map : vipMapList) {
            vipMap.put(Long.parseLong(map.get("app_id").toString()), Long.parseLong(map.get("total_user").toString()));
        }
        List<SysApp> appList = appMapper.selectSysAppList(new SysApp());
        for (SysApp app : appList) {
            Long appId = app.getAppId();
            SysAppUserCount appUserCount = appUserCountService.selectAppUserCountByAppIdAndCreateTime(appId, DateUtils.getDate());
            if(appUserCount == null) {
                appUserCount = new SysAppUserCount();
                appUserCount.setAppId(appId);
                appUserCount.setCreateTime(DateUtils.getNowDate());
            }
            if(allMap.containsKey(appId)) {
                appUserCount.setTotalNum((long) allMap.get(appId).size());
            } else {
                appUserCount.setTotalNum(0L);
            }
            appUserCount.setLoginNum(loginMap.getOrDefault(appId, 0L));
            appUserCount.setVipNum(vipMap.getOrDefault(appId, 0L));
            log.info("[用户统计]软件ID：{} 用户总数：{} 今日登录用户数{} VIP用户数{}", appUserCount.getAppId(), appUserCount.getTotalNum(), appUserCount.getLoginNum(), appUserCount.getVipNum());
            appUserCountService.saveOrUpdate(appUserCount);
        }
        log.info("[用户统计]用户数据统计完毕");
    }

}
