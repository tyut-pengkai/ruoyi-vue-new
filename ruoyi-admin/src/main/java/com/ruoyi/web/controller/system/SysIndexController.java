package com.ruoyi.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.NoticeType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SysCache;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.sale.mapper.DashboardSaleViewMapper;
import com.ruoyi.system.domain.SysAppUserCount;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.vo.TbhbVo;
import com.ruoyi.system.mapper.SysAppMapper;
import com.ruoyi.system.service.ISysAppUserCountService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.update.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 首页
 *
 * @author ruoyi
 */
@Slf4j
@RestController
public class SysIndexController extends BaseController {
    DecimalFormat df = new DecimalFormat("#.00");
    /**
     * 系统基础配置
     */
    @Autowired
    private RuoYiConfig ruoyiConfig;
    @Resource
    private ISysNoticeService sysNoticeService;
    @Resource
    private DashboardSaleViewMapper saleViewMapper;
    @Resource
    private ISysAppUserCountService appUserCountService;
    @Resource
    private SysAppMapper sysAppMapper;
    @Autowired
    private RedisCache redisCache;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ISysConfigService sysConfigService;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用{}，当前版本：{}，请通过入口地址访问本系统。", ruoyiConfig.getName(), ruoyiConfig.getVersion());
    }

    /**
     * 获取公告
     *
     * @param type 公告类型
     * @return
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/system/getNotice")
    public TableDataInfo getNotice() {
//        Map<String, Object> map = new HashMap<>();
//        // 公告
//        SysNotice sysNotice = new SysNotice();
//        sysNotice.setNoticeType(NoticeType.valueOf(type.name()).getCode());
//        sysNotice.setStatus(UserConstants.NORMAL);
//        SysNotice latestNotice = sysNoticeService.selectLatestNotice(sysNotice);
//        if (latestNotice != null) {
//            map.put("content", latestNotice.getNoticeContent());
//            map.put("title", latestNotice.getNoticeTitle());
//        }
//        return AjaxResult.success(map);
        List<SysNotice> allNoticeList = new ArrayList<>();
        // 获取所有人的本地公告
        SysNotice sysNotice = new SysNotice();
        sysNotice.setNoticeType(NoticeType.BACKEND.getCode());
        sysNotice.setStatus(UserConstants.NORMAL);
        List<SysNotice> noticeList = sysNoticeService.selectNoticeList(sysNotice);
        if(!noticeList.isEmpty()) {
            allNoticeList.addAll(noticeList);
        }
        // 获取代理的本地公告
        if(permissionService.hasRole("agent")) {
            sysNotice.setNoticeType(NoticeType.AGENT.getCode());
            sysNotice.setStatus(UserConstants.NORMAL);
           noticeList = sysNoticeService.selectNoticeList(sysNotice);
            if(!noticeList.isEmpty()) {
                allNoticeList.addAll(noticeList);
            }
        }
        // 获取官方服务器的全局用户公告
        if(permissionService.hasAnyRoles("sadmin,admin")) { // 只给系统管理员显示官方公告
            try {
                String baseUrl = sysConfigService.selectConfigByKey("sys.hyUrl.shop");
                if (StringUtils.isBlank(baseUrl)) {
                    baseUrl = "https://shop.coordsoft.com/";
                }
                baseUrl = Utils.fillUrl(baseUrl);
                String url = baseUrl + "prod-api/system/notice/listAllForUser";
                String remoteJson = Utils.readFromUrl(url);
                AjaxResult noticeTableData = JSON.parseObject(remoteJson, AjaxResult.class);
                JSONArray objectList = (JSONArray) noticeTableData.get(AjaxResult.DATA_TAG);
                if(objectList != null) {
                    noticeList = objectList.toJavaList(SysNotice.class);
                    for (SysNotice notice : noticeList) {
                        if (notice.getNoticeContent().contains("\"/prod-api/")) {
                            String s = notice.getNoticeContent().replaceAll("\"/prod-api/", "\"" + baseUrl + "prod-api/");
                            notice.setNoticeContent(s);
                        }
                    }
                    allNoticeList.addAll(noticeList);
                }
            } catch (Exception e) {
                log.warn("获取官方公告失败");
                e.printStackTrace();
            }
        }
        allNoticeList.sort(Comparator.comparing(BaseEntity::getCreateTime).reversed());
        return getDataTable(allNoticeList);
    }

    /**
     * 获取公告
     *
     * @param type 公告类型
     * @return
     */
    @GetMapping("/index/getDashboard")
    public AjaxResult getDashboard() {
        Map<String, Object> map = new HashMap<>();
        // 总销售额
        BigDecimal totalFee = saleViewMapper.queryTotalFee();
        long totalDays = saleViewMapper.queryTotalDays();
        totalDays = totalDays <= 0 ? 1 : totalDays;
        map.put("totalFee", totalFee);
        map.put("feePerDay", String.format("%2f", totalFee.doubleValue() / totalDays) );
        TbhbVo rtbhb = handleRate(saleViewMapper.queryRtbhb());
        TbhbVo ztbhb = handleRate(saleViewMapper.queryZtbhb());
        TbhbVo ytbhb = handleRate(saleViewMapper.queryYtbhb());
        TbhbVo ntbhb = handleRate(saleViewMapper.queryNtbhb());
        map.put("tb", String.format("%s|%s|%s|%s", rtbhb.getTbRate(), ztbhb.getTbRate(), ytbhb.getTbRate(), ntbhb.getTbRate()));
        map.put("hb", String.format("%s|%s|%s|%s", rtbhb.getHbRate(), ztbhb.getHbRate(), ytbhb.getHbRate(), ntbhb.getHbRate()));
        // 支付笔数
        int totalTradeAll = saleViewMapper.queryTotalTradeAll();
        int totalTrade = saleViewMapper.queryTotalTrade();
        map.put("totalTradeAll", totalTradeAll);
        map.put("totalTrade", totalTrade);
        double rate = (double) totalTrade * 100 / totalTradeAll;
        map.put("transRate", String.format("%.2f", rate));
        // 柱状图
        LocalDate localDate = LocalDate.now();
        LocalDate firstDay = localDate.minusDays(10);
        List<Integer> tradeNumList = new ArrayList<>();
        List<String> dateWeekList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LocalDate previous = firstDay.plusDays(i);
            LocalDate next = previous.plusDays(1);
            int num = saleViewMapper.queryTotalTradeBetween(previous.toString(), next.toString());
            tradeNumList.add(num);
            dateWeekList.add(previous.toString().replaceAll("-", "/"));
        }
        map.put("dateWeekList", dateWeekList);
        map.put("tradeNumList", tradeNumList);
        // 用户统计
        List<DataVo> dataVoList = new ArrayList<>();
        // 用户总数量
        SysApp sysApp = new SysApp();
        sysApp.setStatus(UserConstants.NORMAL);
        List<SysApp> appList = sysAppMapper.selectSysAppList(sysApp);
        List<List<SysAppUserCount>> appUserCountListList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LambdaQueryWrapper<SysAppUserCount> dataVoWrapper = new LambdaQueryWrapper<>();
            LocalDate previous = firstDay.plusDays(i);
            LocalDate next = previous.plusDays(1);
            dataVoWrapper.between(SysAppUserCount::getCreateTime, previous.toString(), next.toString());
            List<SysAppUserCount> list = appUserCountService.list(dataVoWrapper);
            appUserCountListList.add(list);
        }
        Map<String, Map<String, Map<String, Object>>> appUserCountMap = new HashMap<>();
        final String[] numType = new String[] {"totalNum", "loginNum", "vipNum", "maxOnlineNum"};
        final String[] numTypeName = new String[] {"总用户数", "登录用户", "VIP用户", "最高在线"};
        for (int i = 0; i < appUserCountListList.size(); i++) {
            List<SysAppUserCount> mapList = appUserCountListList.get(i);
            for (SysAppUserCount item : mapList) {
                String appId = item.getAppId().toString();
                appUserCountMap.computeIfAbsent(appId, key->new HashMap<>());
                for (String type : numType) {
                    appUserCountMap.get(appId).computeIfAbsent(type, key->new HashMap<>());
                    ArrayList<Long> list = (ArrayList<Long>) appUserCountMap.get(appId).get(type).computeIfAbsent("data", key -> new ArrayList<>());
                    switch (type) {
                        case "totalNum": list.add(item.getTotalNum());break;
                        case "loginNum": list.add(item.getLoginNum());break;
                        case "vipNum": list.add(item.getVipNum());break;
                        case "maxOnlineNum": list.add(item.getMaxOnlineNum());break;
                    }
                }
            }
            for (SysApp app : appList) {
                String appId = app.getAppId().toString();
                appUserCountMap.computeIfAbsent(appId, key->new HashMap<>());
                for (String type : numType) {
                    appUserCountMap.get(appId).computeIfAbsent(type, key -> new HashMap<>());
                    ArrayList<Long> list = (ArrayList<Long>) appUserCountMap.get(appId).get(type).computeIfAbsent("data", key -> new ArrayList<>());
                    for (Long l : list) {
                        if(l == null) {
                            l = 0L;
                        }
                    }
                    while (list.size() <= i) {
                        list.add(0L);
                    }
                }
            }
        }
        for (SysApp app : appList) {
            String appId = app.getAppId().toString();
            String appName = app.getAppName();
            for (String type : numType) {
                appUserCountMap.get(appId).computeIfAbsent(type, key -> new HashMap<>());
                appUserCountMap.get(appId).get(type).put("appName", appName);
            }
        }
        // 过滤
        List<String> appIdList = appList.stream().map(app -> app.getAppId().toString()).collect(Collectors.toList());
        Set<String> keySet2 = new HashSet<>(appUserCountMap.keySet());
        for (String appId : keySet2) {
            if (!appIdList.contains(appId)) {
                appUserCountMap.remove(appId);
            }
        }
        for (Map<String, Map<String, Object>> value : appUserCountMap.values()) {
            for (Map.Entry<String, Map<String, Object>> entry : value.entrySet()) {
                String key = entry.getKey();
                Map<String, Object> map1 = entry.getValue();
                String appName = map1.get("appName").toString();
                ArrayList<Long> data = (ArrayList<Long>) map1.get("data");
                for (Long datum : data) {
                    if(datum != null) {
                        dataVoList.add(new DataVo(appName, "line", false, key, data));
                        break;
                    }
                }
            }
        }
        map.put("appUserCount", dataVoList);
        // 实时在线
        Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<LoginUser> onlineListU = new ArrayList<>();
        for (String key : keys) {
            LoginUser loginUser = null;
            try {
                loginUser = (LoginUser) SysCache.get(key);
            } catch(Exception ignored) {}
            if(loginUser == null) {
                try {
                    loginUser = redisCache.getCacheObject(key);
                    SysCache.set(key, loginUser, redisCache.getExpire(key));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (loginUser != null && loginUser.getIfApp() && !loginUser.getIfTrial()) {
                onlineListU.add(loginUser);
            }
        }
        Map<Long, List<LoginUser>> collected = onlineListU.stream().collect(Collectors.groupingBy(LoginUser::getAppId));
        Map<Long, String> appIdNameMap = appList.stream().collect(Collectors.toMap(SysApp::getAppId, SysApp::getAppName));
        List<Map<String, Object>> onlineUserList = new ArrayList<>();
        for (Map.Entry<Long, List<LoginUser>> entry : collected.entrySet()) {
            Long key = entry.getKey();
            List<LoginUser> value = entry.getValue();
            Map<String, Object> map1 = new HashMap<>();
            map1.put("name", appIdNameMap.get(key));
            map1.put("value", value.size());
            onlineUserList.add(map1);
        }
        map.put("onlineUser", onlineUserList);
        map.put("onlineUserNum", keys.size());
        // 软件版本直达
        ArrayList<SysApp> apps = new ArrayList<>(appList);
        apps = new ArrayList<>(apps.subList(0, Math.min(apps.size(), 6)));
        apps.sort(Comparator.comparingLong(SysApp::getAppId));
        List<Object> collect = apps.stream().map(item -> {
            HashMap<Object, Object> map1 = new HashMap<>();
            map1.put("appId", item.getAppId());
            map1.put("appName", item.getAppName());
            map1.put("appType", (item.getAuthType() == AuthType.ACCOUNT?"账号":"单码") + (item.getBillType()== BillType.TIME ? "计时": "计点"));
            return map1;
        }).collect(Collectors.toList());
        map.put("appList", collect);
        // 软件版本直达更多
        ArrayList<SysApp> apps2 = new ArrayList<>(appList);
        if(appList.size()>6) {
            apps2 = new ArrayList<>(apps2.subList(6, apps2.size()));
            apps2.sort(Comparator.comparingLong(SysApp::getAppId));
            List<Object> collect2 = apps2.stream().map(item -> {
                HashMap<Object, Object> map1 = new HashMap<>();
                map1.put("appId", item.getAppId());
                map1.put("appName", item.getAppName());
                map1.put("appType", (item.getAuthType() == AuthType.ACCOUNT?"账号":"单码") + (item.getBillType()== BillType.TIME ? "计时": "计点"));
                return map1;
            }).collect(Collectors.toList());
            map.put("appListMore", collect2);
        } else {
            map.put("appListMore", new ArrayList<>());
        }
        return AjaxResult.success(map);
    }

    private TbhbVo handleRate(List<TbhbVo> list) {
        return list.isEmpty() ? new TbhbVo() : list.get(0);
    }

    @Data
    @AllArgsConstructor
    class DataVo {
        private String name; // APP名称
        private String type = "line"; // bar Or line
        private boolean smooth = false;
        private String stack; // 堆叠组名称
        private List<Long> data; // 数值
    }
}
