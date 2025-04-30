package com.ruoyi.web.controller.common;

import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.SysCache;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.payment.constants.PaymentDefine;
import com.ruoyi.sale.mapper.DashboardSaleViewMapper;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysConfigWebsite;
import com.ruoyi.system.mapper.DashboardAppViewMapper;
import com.ruoyi.system.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Resource
    private ServerConfig serverConfig;
    @Resource
    private ISysConfigWebsiteService sysConfigWebsiteService;
    @Resource
    private RuoYiConfig config;
    @Resource
    private ISysAppService sysAppService;
    @Resource
    private DashboardSaleViewMapper saleViewMapper;
    @Resource
    private DashboardAppViewMapper appViewMapper;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysAppUserService sysAppUserService;
    @Resource
    private ISysCardService sysCardService;
    @Resource
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private RedisCache redisCache;
    @Value("${license.licenseServer.enable:#{false}}")
    private boolean isLicenseServer;

    private static final String FILE_DELIMETER = ",";

    private static final Map<Integer, String> convertMap = new HashMap<>();

    static {
        convertMap.put(1, "一");
        convertMap.put(2, "二");
        convertMap.put(3, "三");
        convertMap.put(4, "四");
        convertMap.put(5, "五");
        convertMap.put(6, "六");
        convertMap.put(7, "七");
        convertMap.put(8, "八");
        convertMap.put(9, "九");
        convertMap.put(10, "十");
        convertMap.put(11, "十一");
        convertMap.put(12, "十二");
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
//            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String realFileName = fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            File file = new File(filePath);//读取压缩文件
            long totalSize = file.length(); //获取文件大小
            response.setHeader("Content-Length", String.valueOf(totalSize));

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     */
    @GetMapping("/globalFileDownload/{randomPath}/{fileName}")
    public void globalFileDownload(@PathVariable("randomPath") String randomPath, @PathVariable("fileName") String fileName, boolean delete, HttpServletResponse response) {
        try {
            String filePath = RuoYiConfig.getGlobalFilePath() + File.separator + randomPath + File.separator + fileName;
            File file = new File(filePath);//读取压缩文件
            if (file.exists() && redisCache.hasKey(CacheConstants.GLOBAL_FILE_DOWNLOAD_KEY + randomPath + File.separator + fileName)) {
                long totalSize = file.length(); //获取文件大小
                response.setHeader("Content-Length", String.valueOf(totalSize));
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                FileUtils.setAttachmentResponseHeader(response, fileName);
                FileUtils.writeBytes(filePath, response.getOutputStream());
                if (delete) {
                    FileUtils.deleteFile(filePath);
                    new File(RuoYiConfig.getGlobalFilePath() + File.separator+ randomPath + File.separator).delete();
                }
            } else {
                response.sendError(404, "请求的下载链接无效或已过期");
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files) {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    @GetMapping("/sysInfo")
    @RateLimiter(limitType = LimitType.IP)
    public RuoYiConfig sysInfo() {
        SysConfigWebsite website = sysConfigWebsiteService.getById(1);
        if (StringUtils.isNotBlank(website.getShortName())) {
            config.setShortName(website.getShortName());
        }
        config.setIsLicenseServer(isLicenseServer);
        config.setEnableFrontEnd(UserConstants.YES.equals(website.getEnableFrontEnd()));
        config.setDbVersion(website.getDbVersion());
        config.setDbVersionNo(website.getDbVersionNo());
        return config;
    }

    @GetMapping("/dashboardInfoSaleView")
    public AjaxResult dashboardInfo(boolean showMode) {
        Map<String, Object> map = new HashMap<>();
        // ==================全局统计部分==================
        // 平台总交易额
        BigDecimal feeTotal = saleViewMapper.queryTotalFee();
        map.put("feeTotal", feeTotal);
        // 平台总成交数
        int tradeTotal = saleViewMapper.queryTotalTrade();
        map.put("tradeTotal", tradeTotal);
        // 平台总下单数（含未付款）
        int tradeTotalAll = saleViewMapper.queryTotalTradeAll();
        map.put("tradeTotalAll", tradeTotalAll);
        // 各个软件
        List<Map<String, Object>> mapListTotal = saleViewMapper.queryAppTotalFee();

        if (!showMode) {
            // ===================按天统计部分===============
            // 今日成交
            LocalDate localDate = LocalDate.now();
            String start = localDate.toString();
            String end = localDate.plusDays(1).toString();
            BigDecimal feeToday = saleViewMapper.queryTotalFeeBetween(start, end);
            map.put("feeToday", feeToday);
            int tradeToday = saleViewMapper.queryTotalTradeBetween(start, end);
            map.put("tradeToday", tradeToday);
            // 各个软件
            List<Map<String, Object>> mapListToday = saleViewMapper.queryAppTotalFeeBetween(start, end);
            // 今日下单（含未付款）
            BigDecimal feeTodayAll = saleViewMapper.queryTotalFeeAllBetween(start, end);
            map.put("feeTodayAll", feeTodayAll);
            int tradeTodayAll = saleViewMapper.queryTotalTradeAllBetween(start, end);
            map.put("tradeTodayAll", tradeTodayAll);
            // 昨日成交
            start = localDate.minusDays(1).toString();
            end = localDate.toString();
            BigDecimal feeYesterday = saleViewMapper.queryTotalFeeBetween(start, end);
            map.put("feeYesterday", feeYesterday);
            int tradeYesterday = saleViewMapper.queryTotalTradeBetween(start, end);
            map.put("tradeYesterday", tradeYesterday);
            // 各个软件
            List<Map<String, Object>> mapListYesterday = saleViewMapper.queryAppTotalFeeBetween(start, end);
            // 近七日成交
            start = localDate.minusDays(6).toString();
            end = localDate.plusDays(1).toString();
            BigDecimal feeWeek = saleViewMapper.queryTotalFeeBetween(start, end);
            map.put("feeWeek", feeWeek);
            int tradeWeek = saleViewMapper.queryTotalTradeBetween(start, end);
            map.put("tradeWeek", tradeWeek);
            // 各个软件
            List<Map<String, Object>> mapListWeek = saleViewMapper.queryAppTotalFeeBetween(start, end);
            // 近七日收款类型统计
            List<Map<String, Object>> mapListPayMode = saleViewMapper.queryPayModeBetween(start, end);
            List<Map<String, Object>> payModeList = new ArrayList<>();
            for (Map<String, Object> item : mapListPayMode) {
                Map<String, Object> mapPayMode = new HashMap<>();
                String payMode = item.get("pay_mode").toString();
                Object totalCount = item.get("total_count");
                if ("manual".equals(payMode)) {
                    mapPayMode.put("payMode", "人工");
                } else if (PaymentDefine.paymentMap.containsKey(payMode)) {
                    mapPayMode.put("payMode", PaymentDefine.paymentMap.get(payMode).getName());
                } else {
                    mapPayMode.put("payMode", payMode);
//                    continue; // 丢弃
                }
                mapPayMode.put("totalCount", totalCount);
                payModeList.add(mapPayMode);
            }
            map.put("payModeList", payModeList);

            // 各软件详细数据
            List<SysApp> appList = sysAppService.selectSysAppList(new SysApp());
            Map<String, Map<String, Object>> feeAppMap = new HashMap<>();
            for (Map<String, Object> item : mapListTotal) {
                String appId = item.get("app_id").toString();
                Object feeAppTotal = item.get("total_fee");
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                feeAppMap.get(appId).put("feeTotal", feeAppTotal);
            }
            for (Map<String, Object> item : mapListToday) {
                String appId = item.get("app_id").toString();
                Object feeAppToday = item.get("total_fee");
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                feeAppMap.get(appId).put("feeToday", feeAppToday);
            }
            for (Map<String, Object> item : mapListYesterday) {
                String appId = item.get("app_id").toString();
                Object feeAppYesterday = item.get("total_fee");
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                feeAppMap.get(appId).put("feeYesterday", feeAppYesterday);
            }
            for (Map<String, Object> item : mapListWeek) {
                String appId = item.get("app_id").toString();
                Object feeAppWeek = item.get("total_fee");
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                feeAppMap.get(appId).put("feeWeek", feeAppWeek);
            }
            String[] keys = new String[]{"feeTotal", "feeToday", "feeYesterday", "feeWeek"};

            List<String> appIdList = appList.stream().map(app -> app.getAppId().toString()).collect(Collectors.toList());

            for (SysApp app : appList) {
                String appId = app.getAppId().toString();
                String appName = app.getAppName();
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                for (String key : keys) {
                    if (!feeAppMap.get(appId).containsKey(key)) {
                        feeAppMap.get(appId).put(key, BigDecimal.ZERO);
                    }
                }
                feeAppMap.get(appId).put("appName", appName);
            }
            // 过滤
            Set<String> keySet = new HashSet<>(feeAppMap.keySet());
            for (String appId : keySet) {
                if (!appIdList.contains(appId)) {
                    feeAppMap.remove(appId);
                }
            }
            ArrayList<Map<String, Object>> feeAppList = new ArrayList<>(feeAppMap.values());
            feeAppList.sort((o1, o2) -> ((BigDecimal) o2.get("feeTotal")).compareTo(((BigDecimal) o1.get("feeTotal"))));
            map.put("feeAppList", feeAppList);
            // 近七天数据（直方图和折线图）
//            LocalDate monday = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate firstDay = localDate.minusDays(6);
            List<List<Map<String, Object>>> mapListList = new ArrayList<>();
            List<String> dateWeekList = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                LocalDate previous = firstDay.plusDays(i);
                LocalDate next = previous.plusDays(1);
                List<Map<String, Object>> tempMapList = saleViewMapper.queryAppTotalFeeBetween(previous.toString(), next.toString());
                mapListList.add(tempMapList);
                dateWeekList.add(previous.toString().replaceAll("-", "/"));
            }
            Map<String, Map<String, Object>> feeAppWeekMap = new HashMap<>();
            for (int i = 0; i < mapListList.size(); i++) {
                List<Map<String, Object>> mapList = mapListList.get(i);
                for (Map<String, Object> item : mapList) {
                    String appId = item.get("app_id").toString();
                    Object feeApp = item.get("total_fee");
                    if (!feeAppWeekMap.containsKey(appId)) {
                        feeAppWeekMap.put(appId, new HashMap<>());
                    }
                    if (!feeAppWeekMap.get(appId).containsKey("data")) {
                        feeAppWeekMap.get(appId).put("data", new ArrayList<>());
                    }
                    ((ArrayList<Object>) feeAppWeekMap.get(appId).get("data")).add(feeApp);
                }
                for (SysApp app : appList) {
                    String appId = app.getAppId().toString();
                    if (!feeAppWeekMap.containsKey(appId)) {
                        feeAppWeekMap.put(appId, new HashMap<>());
                        feeAppWeekMap.get(appId).put("data", new ArrayList<>());
                    }
                    ArrayList<Object> data = (ArrayList<Object>) feeAppWeekMap.get(appId).get("data");
                    while (data.size() <= i) {
                        data.add(BigDecimal.ZERO);
                    }
                }
            }
            for (SysApp app : appList) {
                String appId = app.getAppId().toString();
                String appName = app.getAppName();
                feeAppWeekMap.get(appId).put("appName", appName);
            }
            // 过滤
            Set<String> keySet2 = new HashSet<>(feeAppWeekMap.keySet());
            for (String appId : keySet2) {
                if (!appIdList.contains(appId)) {
                    feeAppWeekMap.remove(appId);
                }
            }
            map.put("dateWeekList", dateWeekList);
            map.put("feeAppWeekList", feeAppWeekMap.values());
        } else {
            // ===================按月统计部分===============
            // 本月成交
            LocalDate localDate = LocalDate.now();
            String start = localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
            String end = localDate.with(TemporalAdjusters.firstDayOfNextMonth()).toString();
            BigDecimal feeToday = saleViewMapper.queryTotalFeeBetween(start, end);
            map.put("feeToday", feeToday);
            int tradeToday = saleViewMapper.queryTotalTradeBetween(start, end);
            map.put("tradeToday", tradeToday);
            // 各个软件
            List<Map<String, Object>> mapListToday = saleViewMapper.queryAppTotalFeeBetween(start, end);
            // 本月下单（含未付款）
            BigDecimal feeTodayAll = saleViewMapper.queryTotalFeeAllBetween(start, end);
            map.put("feeTodayAll", feeTodayAll);
            int tradeTodayAll = saleViewMapper.queryTotalTradeAllBetween(start, end);
            map.put("tradeTodayAll", tradeTodayAll);
            // 上月成交
            LocalDate lastDayOfLastMonth = localDate.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
            start = lastDayOfLastMonth.with(TemporalAdjusters.firstDayOfMonth()).toString();
            end = localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
            BigDecimal feeYesterday = saleViewMapper.queryTotalFeeBetween(start, end);
            map.put("feeYesterday", feeYesterday);
            int tradeYesterday = saleViewMapper.queryTotalTradeBetween(start, end);
            map.put("tradeYesterday", tradeYesterday);
            // 各个软件
            List<Map<String, Object>> mapListYesterday = saleViewMapper.queryAppTotalFeeBetween(start, end);
            // 近半年成交
            start = localDate.minusMonths(6).toString();
            end = localDate.plusDays(1).toString();
            BigDecimal feeWeek = saleViewMapper.queryTotalFeeBetween(start, end);
            map.put("feeWeek", feeWeek);
            int tradeWeek = saleViewMapper.queryTotalTradeBetween(start, end);
            map.put("tradeWeek", tradeWeek);
            // 各个软件
            List<Map<String, Object>> mapListWeek = saleViewMapper.queryAppTotalFeeBetween(start, end);
            // 近半年收款类型统计
            List<Map<String, Object>> mapListPayMode = saleViewMapper.queryPayModeBetween(start, end);
            List<Map<String, Object>> payModeList = new ArrayList<>();
            for (Map<String, Object> item : mapListPayMode) {
                Map<String, Object> mapPayMode = new HashMap<>();
                String payMode = item.get("pay_mode").toString();
                Object totalCount = item.get("total_count");
                if ("manual".equals(payMode)) {
                    mapPayMode.put("payMode", "人工");
                } else if (PaymentDefine.paymentMap.containsKey(payMode)) {
                    mapPayMode.put("payMode", PaymentDefine.paymentMap.get(payMode).getName());
                } else {
                    mapPayMode.put("payMode", payMode);
//                    continue; // 丢弃
                }
                mapPayMode.put("totalCount", totalCount);
                payModeList.add(mapPayMode);
            }
            map.put("payModeList", payModeList);

            // 各软件详细数据
            List<SysApp> appList = sysAppService.selectSysAppList(new SysApp());
            Map<String, Map<String, Object>> feeAppMap = new HashMap<>();
            for (Map<String, Object> item : mapListTotal) {
                String appId = item.get("app_id").toString();
                Object feeAppTotal = item.get("total_fee");
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                feeAppMap.get(appId).put("feeTotal", feeAppTotal);
            }
            for (Map<String, Object> item : mapListToday) {
                String appId = item.get("app_id").toString();
                Object feeAppToday = item.get("total_fee");
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                feeAppMap.get(appId).put("feeToday", feeAppToday);
            }
            for (Map<String, Object> item : mapListYesterday) {
                String appId = item.get("app_id").toString();
                Object feeAppYesterday = item.get("total_fee");
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                feeAppMap.get(appId).put("feeYesterday", feeAppYesterday);
            }
            for (Map<String, Object> item : mapListWeek) {
                String appId = item.get("app_id").toString();
                Object feeAppWeek = item.get("total_fee");
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                feeAppMap.get(appId).put("feeWeek", feeAppWeek);
            }
            String[] keys = new String[]{"feeTotal", "feeToday", "feeYesterday", "feeWeek"};

            List<String> appIdList = appList.stream().map(app -> app.getAppId().toString()).collect(Collectors.toList());

            for (SysApp app : appList) {
                String appId = app.getAppId().toString();
                String appName = app.getAppName();
                if (!feeAppMap.containsKey(appId)) {
                    feeAppMap.put(appId, new HashMap<>());
                }
                for (String key : keys) {
                    if (!feeAppMap.get(appId).containsKey(key)) {
                        feeAppMap.get(appId).put(key, BigDecimal.ZERO);
                    }
                }
                feeAppMap.get(appId).put("appName", appName);
            }
            // 过滤
            Set<String> keySet = new HashSet<>(feeAppMap.keySet());
            for (String appId : keySet) {
                if (!appIdList.contains(appId)) {
                    feeAppMap.remove(appId);
                }
            }
            ArrayList<Map<String, Object>> feeAppList = new ArrayList<>(feeAppMap.values());
            feeAppList.sort((o1, o2) -> ((BigDecimal) o2.get("feeTotal")).compareTo(((BigDecimal) o1.get("feeTotal"))));
            map.put("feeAppList", feeAppList);
            // 近半年数据（直方图和折线图）
//            LocalDate monday = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate monday = localDate.minusMonths(5);
            List<List<Map<String, Object>>> mapListList = new ArrayList<>();
            List<String> dateWeekList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                LocalDate previous = monday.plusMonths(i).with(TemporalAdjusters.firstDayOfMonth());
                LocalDate next = previous.with(TemporalAdjusters.firstDayOfNextMonth());
                List<Map<String, Object>> tempMapList = saleViewMapper.queryAppTotalFeeBetween(previous.toString(), next.toString());
                mapListList.add(tempMapList);
//                dateWeekList.add(convertMap.get(previous.getMonthValue()) + "-" + convertMap.get(next.getMonthValue()) + "月");
                dateWeekList.add(previous.getMonthValue() + "月");
            }
            Map<String, Map<String, Object>> feeAppWeekMap = new HashMap<>();
            for (int i = 0; i < mapListList.size(); i++) {
                List<Map<String, Object>> mapList = mapListList.get(i);
                for (Map<String, Object> item : mapList) {
                    String appId = item.get("app_id").toString();
                    Object feeApp = item.get("total_fee");
                    if (!feeAppWeekMap.containsKey(appId)) {
                        feeAppWeekMap.put(appId, new HashMap<>());
                    }
                    if (!feeAppWeekMap.get(appId).containsKey("data")) {
                        feeAppWeekMap.get(appId).put("data", new ArrayList<>());
                    }
                    ((ArrayList<Object>) feeAppWeekMap.get(appId).get("data")).add(feeApp);
                }
                for (SysApp app : appList) {
                    String appId = app.getAppId().toString();
                    if (!feeAppWeekMap.containsKey(appId)) {
                        feeAppWeekMap.put(appId, new HashMap<>());
                        feeAppWeekMap.get(appId).put("data", new ArrayList<>());
                    }
                    ArrayList<Object> data = (ArrayList<Object>) feeAppWeekMap.get(appId).get("data");
                    while (data.size() <= i) {
                        data.add(BigDecimal.ZERO);
                    }
                }
            }
            for (SysApp app : appList) {
                String appId = app.getAppId().toString();
                String appName = app.getAppName();
                feeAppWeekMap.get(appId).put("appName", appName);
            }
            // 过滤
            Set<String> keySet2 = new HashSet<>(feeAppWeekMap.keySet());
            for (String appId : keySet2) {
                if (!appIdList.contains(appId)) {
                    feeAppWeekMap.remove(appId);
                }
            }
            map.put("dateWeekList", dateWeekList);
            map.put("feeAppWeekList", feeAppWeekMap.values());
        }
        return AjaxResult.success(map);
    }

    @GetMapping("/dashboardInfoAppView")
    public AjaxResult dashboardInfoAppView(boolean showMode) {

        Map<String, Object> map = new HashMap<>();
        // ==================全局统计部分==================
        // 平台账号总数
        int userTotal = sysUserService.selectUserList(new SysUser()).size();
        map.put("userTotal", userTotal);
        // 平台用户总数
        int appUserTotal = sysAppUserService.selectSysAppUserList(new SysAppUser()).size();
        map.put("appUserTotal", appUserTotal);
        // 平台VIP用户数
        int appUserVipTotal = appViewMapper.queryAppUserVipTotal();
        map.put("appUserVipTotal", appUserVipTotal);
        // 平台当前在线数
        List<LoginUser> onlineUserList = new ArrayList<>();
        Collection<String> loginKeys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*");
        for (String key : loginKeys) {
            LoginUser loginUser = null;
            try {
                loginUser = (LoginUser) SysCache.get(key);
            } catch(Exception ignored) {}
            if(loginUser == null) {
                loginUser = redisCache.getCacheObject(key);
                SysCache.set(key, loginUser, redisCache.getExpire(key));
            }
            if (loginUser != null && loginUser.getIfApp()) {
                onlineUserList.add(loginUser);
            }
        }
        int onlineTotal = onlineUserList.size();
        map.put("onlineTotal", onlineTotal);
        // 平台充值卡数量
        int cardTotal = sysCardService.countSysCard(new SysCard());
        map.put("cardTotal", cardTotal);
        // 平台已激活充值卡数量
        SysCard cardActiveObj = new SysCard();
        cardActiveObj.setIsCharged("Y");
        int cardActive = sysCardService.countSysCard(cardActiveObj);
        map.put("cardActive", cardActive);
        // 平台未激活充值卡数量
        SysCard cardNoActiveObj = new SysCard();
        cardNoActiveObj.setIsCharged("N");
        int cardNoActive = sysCardService.countSysCard(cardNoActiveObj);
        map.put("cardNoActive", cardNoActive);
        // 平台登录码数量
        int loginCodeTotal = sysLoginCodeService.countSysLoginCode(new SysLoginCode());
        map.put("loginCodeTotal", loginCodeTotal);
        // 平台已激活登录码数量
        SysLoginCode loginCodeActiveObj = new SysLoginCode();
        loginCodeActiveObj.setIsCharged("Y");
        int loginCodeActive = sysLoginCodeService.countSysLoginCode(loginCodeActiveObj);
        map.put("loginCodeActive", loginCodeActive);
        // 平台未激活登录码数量
        SysLoginCode loginCodeNoActiveObj = new SysLoginCode();
        loginCodeNoActiveObj.setIsCharged("N");
        int loginCodeNoActive = sysLoginCodeService.countSysLoginCode(loginCodeNoActiveObj);
        map.put("loginCodeNoActive", loginCodeNoActive);
        // 各个软件
        List<Map<String, Object>> mapListAppUser = appViewMapper.queryAppUser();
        List<Map<String, Object>> mapListAppUserVip = appViewMapper.queryAppUserVip();

        if (!showMode) {
            // ===================按天统计部分===============
            // 平台用户总数-今日新增
            LocalDate localDate = LocalDate.now();
            String start = localDate.toString();
            String end = localDate.plusDays(1).toString();
            int appUserToday = appViewMapper.queryAppUserTotalBetween(start, end);
            map.put("appUserToday", appUserToday);
            // 平台VIP用户总数-今日新增
            int appUserVipToday = appViewMapper.queryAppUserVipTotalBetween(start, end);
            map.put("appUserVipToday", appUserVipToday);
            // 平台今日登录用户总数
            int loginAppUserToday = appViewMapper.queryLoginAppUserTotalBetween(start, end);
            map.put("loginAppUserToday", loginAppUserToday);
            // 平台今日新增充值卡数量
            int cardToday = appViewMapper.queryCardTotalBetween(start, end);
            map.put("cardToday", cardToday);
            // 平台今日新增激活充值卡数量
            int cardActiveToday = appViewMapper.queryCardActiveBetween(start, end);
            map.put("cardActiveToday", cardActiveToday);
            // 平台今日新增未激活充值卡数量
            int cardNoActiveToday = appViewMapper.queryCardNoActiveBetween(start, end);
            map.put("cardNoActiveToday", cardNoActiveToday);
            // 平台今日新增登录码数量
            int loginCodeToday = appViewMapper.queryLoginCodeTotalBetween(start, end);
            map.put("loginCodeToday", loginCodeToday);
            // 平台今日新增激活登录码数量
            int loginCodeActiveToday = appViewMapper.queryLoginCodeActiveBetween(start, end);
            map.put("loginCodeActiveToday", loginCodeActiveToday);
            // 平台今日新增未激活登录码数量
            int loginCodeNoActiveToday = appViewMapper.queryLoginCodeNoActiveBetween(start, end);
            map.put("loginCodeNoActiveToday", loginCodeNoActiveToday);
            // 各个软件
            List<Map<String, Object>> mapListLoginAppUser = appViewMapper.queryAppLoginAppUserTotalBetween(start, end);


            List<SysApp> appList = sysAppService.selectSysAppList(new SysApp());
            List<String> appIdList = appList.stream().map(app -> app.getAppId().toString()).collect(Collectors.toList());
            // 近七日激活充值卡-APP角度
            start = localDate.minusDays(6).toString();
            end = localDate.plusDays(1).toString();
            List<Map<String, Object>> mapListCardActive = appViewMapper.queryAppCardActiveBetween(start, end);
            Map<String, Map<String, Object>> cardActiveMap = new HashMap<>();
            for (Map<String, Object> item : mapListCardActive) {
                Map<String, Object> mapAppCardActive = new HashMap<>();
                String appId = item.get("app_id").toString();
                String appName = item.get("app_name").toString();
                Object totalCount = item.get("total_count");
                mapAppCardActive.put("appId", appId);
                mapAppCardActive.put("appName", appName);
                mapAppCardActive.put("totalCount", totalCount);
                cardActiveMap.put(appId, mapAppCardActive);
            }
            // 过滤
            Set<String> keySet = new HashSet<>(cardActiveMap.keySet());
            for (String appId : keySet) {
                if (!appIdList.contains(appId)) {
                    cardActiveMap.remove(appId);
                }
            }
            map.put("cardActiveList", cardActiveMap.values());

            // 近七日激活充值卡-卡类角度
            start = localDate.minusDays(6).toString();
            end = localDate.plusDays(1).toString();
            List<Map<String, Object>> mapListCardActive2 = appViewMapper.queryTemplateCardActiveBetween(start, end);
            Map<String, Map<String, Object>> cardActiveMap2 = new HashMap<>();
            for (Map<String, Object> item : mapListCardActive2) {
                Map<String, Object> mapAppCardActive = new HashMap<>();
                String appId = item.get("app_id").toString();
                String appName = item.get("app_name").toString();
                String templateId = item.get("template_id").toString();
                String cardName = item.get("card_name").toString();
                Object totalCount = item.get("total_count");
                mapAppCardActive.put("templateId", templateId);
                mapAppCardActive.put("cardName", "[" + appName + "]" + cardName);
                mapAppCardActive.put("totalCount", totalCount);
                cardActiveMap2.put(appId, mapAppCardActive);
            }
            // 过滤
            Set<String> keySet4 = new HashSet<>(cardActiveMap2.keySet());
            for (String appId : keySet4) {
                if (!appIdList.contains(appId)) {
                    cardActiveMap2.remove(appId);
                }
            }
            map.put("cardActiveList2", cardActiveMap2.values());

            // 各软件详细数据
            Map<String, Map<String, Object>> appDataMap = new HashMap<>();
            for (Map<String, Object> item : mapListAppUser) {
                String appId = item.get("app_id").toString();
                Object totalUser = item.get("total_user");
                if (!appDataMap.containsKey(appId)) {
                    appDataMap.put(appId, new HashMap<>());
                }
                appDataMap.get(appId).put("appUserTotal", totalUser);
            }
            for (Map<String, Object> item : mapListAppUserVip) {
                String appId = item.get("app_id").toString();
                Object totalUser = item.get("total_user");
                if (!appDataMap.containsKey(appId)) {
                    appDataMap.put(appId, new HashMap<>());
                }
                appDataMap.get(appId).put("appUserVipTotal", totalUser);
            }
            for (Map<String, Object> item : mapListLoginAppUser) {
                String appId = item.get("app_id").toString();
                Object totalUser = item.get("total_user");
                if (!appDataMap.containsKey(appId)) {
                    appDataMap.put(appId, new HashMap<>());
                }
                appDataMap.get(appId).put("loginToday", totalUser);
            }
            // 当前在线
            for (LoginUser user : onlineUserList) {
                if (user.getIfApp()) {
                    String appKey = user.getAppKey();
                    if (appKey == null) {
                        appKey = user.getApp() != null ? user.getApp().getAppKey() : null;
                    }
                    if (StringUtils.isNotBlank(appKey)) {
                        SysApp app = sysAppService.selectSysAppByAppKey(appKey);
                        if(app != null) {
                            String appId = app.getAppId().toString();
                            if (!appDataMap.containsKey(appId)) {
                                appDataMap.put(appId, new HashMap<>());
                            }
                            if (appDataMap.get(appId).containsKey("online")) {
                                appDataMap.get(appId).put("online", (int) (appDataMap.get(appId).get("online")) + 1);
                            } else {
                                appDataMap.get(appId).put("online", 1);
                            }
                        }
                    }
                }
            }

            String[] keys = new String[]{"appUserTotal", "appUserVipTotal", "loginToday", "online"};
            for (SysApp app : appList) {
                String appId = app.getAppId().toString();
                String appName = app.getAppName();
                if (!appDataMap.containsKey(appId)) {
                    appDataMap.put(appId, new HashMap<>());
                }
                for (String key : keys) {
                    if (!appDataMap.get(appId).containsKey(key)) {
                        appDataMap.get(appId).put(key, 0L);
                    }
                }
                appDataMap.get(appId).put("appName", appName);
            }
            // 过滤
            Set<String> keySet3 = new HashSet<>(appDataMap.keySet());
            for (String appId : keySet3) {
                if (!appIdList.contains(appId)) {
                    appDataMap.remove(appId);
                }
            }
            ArrayList<Map<String, Object>> appDataList = new ArrayList<>(appDataMap.values());
            appDataList.sort((o1, o2) -> (((Long) o2.get("appUserTotal")).compareTo(((Long) o1.get("appUserTotal"))) == 0 ?
                    ((Long) o2.get("appUserVipTotal")).compareTo(((Long) o1.get("appUserVipTotal"))) :
                    ((Long) o2.get("appUserTotal")).compareTo(((Long) o1.get("appUserTotal")))));
            map.put("appDataList", appDataList);

            // 近七天数据（直方图和折线图）
            LocalDate firstDay = localDate.minusDays(6);
            List<List<Map<String, Object>>> mapListList = new ArrayList<>();
            List<String> dateWeekList = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                LocalDate previous = firstDay.plusDays(i);
                LocalDate next = previous.plusDays(1);
                List<Map<String, Object>> tempMapList = appViewMapper.queryAppUserBetween(previous.toString(), next.toString());
                mapListList.add(tempMapList);
                dateWeekList.add(previous.toString().replaceAll("-", "/"));
            }
            Map<String, Map<String, Object>> increaseUserWeekMap = new HashMap<>();
            for (int i = 0; i < mapListList.size(); i++) {
                List<Map<String, Object>> mapList = mapListList.get(i);
                for (Map<String, Object> item : mapList) {
                    String appId = item.get("app_id").toString();
                    Object totalUser = item.get("total_user");
                    if (!increaseUserWeekMap.containsKey(appId)) {
                        increaseUserWeekMap.put(appId, new HashMap<>());
                    }
                    if (!increaseUserWeekMap.get(appId).containsKey("data")) {
                        increaseUserWeekMap.get(appId).put("data", new ArrayList<>());
                    }
                    ((ArrayList<Object>) increaseUserWeekMap.get(appId).get("data")).add(totalUser);
                }
                for (SysApp app : appList) {
                    String appId = app.getAppId().toString();
                    if (!increaseUserWeekMap.containsKey(appId)) {
                        increaseUserWeekMap.put(appId, new HashMap<>());
                        increaseUserWeekMap.get(appId).put("data", new ArrayList<>());
                    }
                    ArrayList<Object> data = (ArrayList<Object>) increaseUserWeekMap.get(appId).get("data");
                    while (data.size() <= i) {
                        data.add(0L);
                    }
                }
            }
            for (SysApp app : appList) {
                String appId = app.getAppId().toString();
                String appName = app.getAppName();
                increaseUserWeekMap.get(appId).put("appName", appName);
            }
            // 过滤
            Set<String> keySet2 = new HashSet<>(increaseUserWeekMap.keySet());
            for (String appId : keySet2) {
                if (!appIdList.contains(appId)) {
                    increaseUserWeekMap.remove(appId);
                }
            }
            map.put("dateWeekList", dateWeekList);
            map.put("increaseUserWeekList", increaseUserWeekMap.values());

        } else {
            // ===================按月统计部分===============
            // 本月成交
            LocalDate localDate = LocalDate.now();
            String start = localDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
            String end = localDate.with(TemporalAdjusters.firstDayOfNextMonth()).toString();

            int appUserToday = appViewMapper.queryAppUserTotalBetween(start, end);
            map.put("appUserToday", appUserToday);
            // 平台VIP用户总数-本月新增
            int appUserVipToday = appViewMapper.queryAppUserVipTotalBetween(start, end);
            map.put("appUserVipToday", appUserVipToday);
            // 平台本月登录用户总数
            int loginAppUserToday = appViewMapper.queryLoginAppUserTotalBetween(start, end);
            map.put("loginAppUserToday", loginAppUserToday);
            // 平台本月新增充值卡数量
            int cardToday = appViewMapper.queryCardTotalBetween(start, end);
            map.put("cardToday", cardToday);
            // 平台本月新增激活充值卡数量
            int cardActiveToday = appViewMapper.queryCardActiveBetween(start, end);
            map.put("cardActiveToday", cardActiveToday);
            // 平台本月新增未激活充值卡数量
            int cardNoActiveToday = appViewMapper.queryCardNoActiveBetween(start, end);
            map.put("cardNoActiveToday", cardNoActiveToday);
            // 平台今日新增登录码数量
            int loginCodeToday = appViewMapper.queryLoginCodeTotalBetween(start, end);
            map.put("loginCodeToday", loginCodeToday);
            // 平台本月新增激活登录码数量
            int loginCodeActiveToday = appViewMapper.queryLoginCodeActiveBetween(start, end);
            map.put("loginCodeActiveToday", loginCodeActiveToday);
            // 平台本月新增未激活登录码数量
            int loginCodeNoActiveToday = appViewMapper.queryLoginCodeNoActiveBetween(start, end);
            map.put("loginCodeNoActiveToday", loginCodeNoActiveToday);
            // 各个软件
            List<Map<String, Object>> mapListLoginAppUser = appViewMapper.queryAppLoginAppUserTotalBetween(start, end);


            List<SysApp> appList = sysAppService.selectSysAppList(new SysApp());
            List<String> appIdList = appList.stream().map(app -> app.getAppId().toString()).collect(Collectors.toList());
            // 近半年激活充值卡-APP角度
            start = localDate.minusMonths(6).toString();
            end = localDate.plusDays(1).toString();
            List<Map<String, Object>> mapListCardActive = appViewMapper.queryAppCardActiveBetween(start, end);
            Map<String, Map<String, Object>> cardActiveMap = new HashMap<>();
            for (Map<String, Object> item : mapListCardActive) {
                Map<String, Object> mapAppCardActive = new HashMap<>();
                String appId = item.get("app_id").toString();
                String appName = item.get("app_name").toString();
                Object totalCount = item.get("total_count");
                mapAppCardActive.put("appId", appId);
                mapAppCardActive.put("appName", appName);
                mapAppCardActive.put("totalCount", totalCount);
                cardActiveMap.put(appId, mapAppCardActive);
            }
            // 过滤
            Set<String> keySet = new HashSet<>(cardActiveMap.keySet());
            for (String appId : keySet) {
                if (!appIdList.contains(appId)) {
                    cardActiveMap.remove(appId);
                }
            }
            map.put("cardActiveList", cardActiveMap.values());

            // 近七日激活充值卡-卡类角度
            List<Map<String, Object>> mapListCardActive2 = appViewMapper.queryTemplateCardActiveBetween(start, end);
            Map<String, Map<String, Object>> cardActiveMap2 = new HashMap<>();
            for (Map<String, Object> item : mapListCardActive2) {
                Map<String, Object> mapAppCardActive = new HashMap<>();
                String appId = item.get("app_id").toString();
                String appName = item.get("app_name").toString();
                String templateId = item.get("template_id").toString();
                String cardName = item.get("card_name").toString();
                Object totalCount = item.get("total_count");
                mapAppCardActive.put("templateId", templateId);
                mapAppCardActive.put("cardName", "[" + appName + "]" + cardName);
                mapAppCardActive.put("totalCount", totalCount);
                cardActiveMap2.put(appId, mapAppCardActive);
            }
            // 过滤
            Set<String> keySet4 = new HashSet<>(cardActiveMap2.keySet());
            for (String appId : keySet4) {
                if (!appIdList.contains(appId)) {
                    cardActiveMap2.remove(appId);
                }
            }
            map.put("cardActiveList2", cardActiveMap2.values());

            // 各软件详细数据
            Map<String, Map<String, Object>> appDataMap = new HashMap<>();
            for (Map<String, Object> item : mapListAppUser) {
                String appId = item.get("app_id").toString();
                Object totalUser = item.get("total_user");
                if (!appDataMap.containsKey(appId)) {
                    appDataMap.put(appId, new HashMap<>());
                }
                appDataMap.get(appId).put("appUserTotal", totalUser);
            }
            for (Map<String, Object> item : mapListAppUserVip) {
                String appId = item.get("app_id").toString();
                Object totalUser = item.get("total_user");
                if (!appDataMap.containsKey(appId)) {
                    appDataMap.put(appId, new HashMap<>());
                }
                appDataMap.get(appId).put("appUserVipTotal", totalUser);
            }
            for (Map<String, Object> item : mapListLoginAppUser) {
                String appId = item.get("app_id").toString();
                Object totalUser = item.get("total_user");
                if (!appDataMap.containsKey(appId)) {
                    appDataMap.put(appId, new HashMap<>());
                }
                appDataMap.get(appId).put("loginToday", totalUser);
            }
            // 当前在线
            for (LoginUser user : onlineUserList) {
                if (user.getIfApp()) {
                    SysApp app = sysAppService.selectSysAppByAppKey(user.getAppKey());
                    String appId = app.getAppId().toString();
                    if (!appDataMap.containsKey(appId)) {
                        appDataMap.put(appId, new HashMap<>());
                    }
                    if (appDataMap.get(appId).containsKey("online")) {
                        appDataMap.get(appId).put("online", (int) (appDataMap.get(appId).get("online")) + 1);
                    } else {
                        appDataMap.get(appId).put("online", 1);
                    }
                }
            }

            String[] keys = new String[]{"appUserTotal", "appUserVipTotal", "loginToday", "online"};
            for (SysApp app : appList) {
                String appId = app.getAppId().toString();
                String appName = app.getAppName();
                if (!appDataMap.containsKey(appId)) {
                    appDataMap.put(appId, new HashMap<>());
                }
                for (String key : keys) {
                    if (!appDataMap.get(appId).containsKey(key)) {
                        appDataMap.get(appId).put(key, 0L);
                    }
                }
                appDataMap.get(appId).put("appName", appName);
            }
            // 过滤
            Set<String> keySet3 = new HashSet<>(appDataMap.keySet());
            for (String appId : keySet3) {
                if (!appIdList.contains(appId)) {
                    appDataMap.remove(appId);
                }
            }
            ArrayList<Map<String, Object>> appDataList = new ArrayList<>(appDataMap.values());
            appDataList.sort((o1, o2) -> (((Long) o2.get("appUserTotal")).compareTo(((Long) o1.get("appUserTotal"))) == 0 ?
                    ((Long) o2.get("appUserVipTotal")).compareTo(((Long) o1.get("appUserVipTotal"))) :
                    ((Long) o2.get("appUserTotal")).compareTo(((Long) o1.get("appUserTotal")))));
            map.put("appDataList", appDataList);

            // 近半年数据（直方图和折线图）
            LocalDate monday = localDate.minusMonths(5);
            List<List<Map<String, Object>>> mapListList = new ArrayList<>();
            List<String> dateWeekList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                LocalDate previous = monday.plusMonths(i).with(TemporalAdjusters.firstDayOfMonth());
                LocalDate next = previous.with(TemporalAdjusters.firstDayOfNextMonth());
                List<Map<String, Object>> tempMapList = appViewMapper.queryAppUserBetween(previous.toString(), next.toString());
                mapListList.add(tempMapList);
                dateWeekList.add(previous.getMonthValue() + "月");
            }
            Map<String, Map<String, Object>> increaseUserWeekMap = new HashMap<>();
            for (int i = 0; i < mapListList.size(); i++) {
                List<Map<String, Object>> mapList = mapListList.get(i);
                for (Map<String, Object> item : mapList) {
                    String appId = item.get("app_id").toString();
                    Object totalUser = item.get("total_user");
                    if (!increaseUserWeekMap.containsKey(appId)) {
                        increaseUserWeekMap.put(appId, new HashMap<>());
                    }
                    if (!increaseUserWeekMap.get(appId).containsKey("data")) {
                        increaseUserWeekMap.get(appId).put("data", new ArrayList<>());
                    }
                    ((ArrayList<Object>) increaseUserWeekMap.get(appId).get("data")).add(totalUser);
                }
                for (SysApp app : appList) {
                    String appId = app.getAppId().toString();
                    if (!increaseUserWeekMap.containsKey(appId)) {
                        increaseUserWeekMap.put(appId, new HashMap<>());
                        increaseUserWeekMap.get(appId).put("data", new ArrayList<>());
                    }
                    ArrayList<Object> data = (ArrayList<Object>) increaseUserWeekMap.get(appId).get("data");
                    while (data.size() <= i) {
                        data.add(0L);
                    }
                }
            }
            for (SysApp app : appList) {
                String appId = app.getAppId().toString();
                String appName = app.getAppName();
                increaseUserWeekMap.get(appId).put("appName", appName);
            }
            // 过滤
            Set<String> keySet2 = new HashSet<>(increaseUserWeekMap.keySet());
            for (String appId : keySet2) {
                if (!appIdList.contains(appId)) {
                    increaseUserWeekMap.remove(appId);
                }
            }
            map.put("dateWeekList", dateWeekList);
            map.put("increaseUserWeekList", increaseUserWeekMap.values());

        }
        return AjaxResult.success(map);
    }

    @GetMapping("/randomString")
    public AjaxResult randomString(int length) {
        return AjaxResult.success(RandomStringUtils.randomAlphanumeric(length));
    }
}
