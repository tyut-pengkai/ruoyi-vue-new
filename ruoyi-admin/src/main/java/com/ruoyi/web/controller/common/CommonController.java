package com.ruoyi.web.controller.common;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.sale.mapper.SysSaleOrderMapper;
import com.ruoyi.system.service.ISysAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private RuoYiConfig config;
    @Resource
    private SysSaleOrderMapper sysSaleOrderMapper;
    @Resource
    private ISysAppService sysAppService;

    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            File file = new File(filePath);//读取压缩文件
            long totalSize = file.length(); //获取文件大小
            response.setHeader("Content-Length", String.valueOf(totalSize));

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
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
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
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
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
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
    public RuoYiConfig sysInfo() {
        return config;
    }

    @GetMapping("/dashboardInfo")
    public AjaxResult dashboardInfo() {
        Map<String, Object> map = new HashMap<>();
        // 平台总交易额
        BigDecimal feeTotal = sysSaleOrderMapper.queryTotalFee();
        map.put("feeTotal", feeTotal);
        // 平台总成交数
        int tradeTotal = sysSaleOrderMapper.queryTotalTrade();
        map.put("tradeTotal", tradeTotal);
        // 平台总下单数（含未付款）
        int tradeTotalAll = sysSaleOrderMapper.queryTotalTradeAll();
        map.put("tradeTotalAll", tradeTotalAll);
        // 各个软件
        List<Map<String, Object>> mapListTotal = sysSaleOrderMapper.queryAppTotalFee();
        // 今日成交
        LocalDate localDate = LocalDate.now();
        String start = localDate.toString();
        String end = localDate.plusDays(1).toString();
        BigDecimal feeToday = sysSaleOrderMapper.queryTotalFeeBetween(start, end);
        map.put("feeToday", feeToday);
        int tradeToday = sysSaleOrderMapper.queryTotalTradeBetween(start, end);
        map.put("tradeToday", tradeToday);
        List<Map<String, Object>> mapListToday = sysSaleOrderMapper.queryAppTotalFeeBetween(start, end);
        // 今日下单（含未付款）
        BigDecimal feeTodayAll = sysSaleOrderMapper.queryTotalFeeAllBetween(start, end);
        map.put("feeTodayAll", feeTodayAll);
        int tradeTodayAll = sysSaleOrderMapper.queryTotalTradeAllBetween(start, end);
        map.put("tradeTodayAll", tradeTodayAll);
        // 昨日成交
        start = localDate.minusDays(1).toString();
        end = localDate.toString();
        BigDecimal feeYesterday = sysSaleOrderMapper.queryTotalFeeBetween(start, end);
        map.put("feeYesterday", feeYesterday);
        int tradeYesterday = sysSaleOrderMapper.queryTotalTradeBetween(start, end);
        map.put("tradeYesterday", tradeYesterday);
        List<Map<String, Object>> mapListYesterday = sysSaleOrderMapper.queryAppTotalFeeBetween(start, end);
        // 近七日成交
        start = localDate.minusDays(6).toString();
        end = localDate.plusDays(1).toString();
        BigDecimal feeWeek = sysSaleOrderMapper.queryTotalFeeBetween(start, end);
        map.put("feeWeek", feeWeek);
        int tradeWeek = sysSaleOrderMapper.queryTotalTradeBetween(start, end);
        map.put("tradeWeek", tradeWeek);
        List<Map<String, Object>> mapListWeek = sysSaleOrderMapper.queryAppTotalFeeBetween(start, end);
        // 获取软件列表
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
        ArrayList<Map<String, Object>> feeAppList = new ArrayList<>(feeAppMap.values());
        feeAppList.sort((o1, o2) -> ((BigDecimal) o2.get("feeTotal")).compareTo(((BigDecimal) o1.get("feeTotal"))));
        map.put("feeAppList", feeAppList);
        // 本周数据


        return AjaxResult.success(map);
    }
}
