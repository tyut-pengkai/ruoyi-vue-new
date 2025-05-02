package com.ruoyi.license.controller;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.api.noAuth.code.RechargeLoginCode;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.LicenseStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.license.bo.LicenseCheckModel;
import com.ruoyi.common.utils.AesCbcPKCS5PaddingUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.api.v1.service.SysAppLoginService;
import com.ruoyi.license.domain.LicenseCreatorParam;
import com.ruoyi.license.domain.SysLicenseRecord;
import com.ruoyi.license.domain.WebsiteLicenseInfo;
import com.ruoyi.license.service.ISysLicenseRecordService;
import com.ruoyi.license.support.LicenseCreator;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysAppVersionService;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.utils.poi.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证授权用户Controller
 *
 * @author zwgu
 * @date 2022-09-08
 */
@Slf4j
@RestController
@RequestMapping("/license/licenseRecord")
public class SysLicenseRecordController extends BaseController {

    @Resource
    private ISysLicenseRecordService sysLicenseRecordService;
    @Resource
    private ISysAppService sysAppService;
    @Resource
    private ISysAppVersionService sysAppVersionService;
    @Resource
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private SysAppLoginService loginService;
    @Resource
    private ISysAppUserService sysAppUserService;

    /**
     * 证书subject
     */
    @Value("${license.subject:#{null}}")
    private String subject;
    /**
     * 授权APP KEY
     */
    @Value("${license.licenseServer.appKey:#{null}}")
    private String appKey;
    /**
     * 授权APP版本号
     */
    @Value("${license.licenseServer.appVersionNo:#{null}}")
    private Long versionNo;

    /**
     * 查询验证授权用户列表
     */
    @PreAuthorize("@ss.hasPermi('license:licenseRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLicenseRecord sysLicenseRecord) {
        startPage();
        List<SysLicenseRecord> list = sysLicenseRecordService.selectSysLicenseRecordList(sysLicenseRecord);
        return getDataTable(list);
    }

    @GetMapping("/getSystemInfo")
    public AjaxResult getSystemInfo(final String webUrl) {
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            if (StringUtils.isNotBlank(webUrl)) {
                Future<Map<String, Object>> future = executorService.submit(() -> {
                    Map<String, Object> map = new HashMap<>();
                    String url = webUrl;
                    url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
                    String response = HttpUtils.sendGet(url + "/prod-api/common/sysInfo");
                    log.info("getWebsiteSysInfoResponse({}): {}", webUrl, response);
                    if (StringUtils.isBlank(response)) {
                        throw new ServiceException("获取目标网站版本信息失败，返回空白信息");
                    }
                    map.put("sysInfo", JSON.parseObject(response, RuoYiConfig.class));
                    String response2 = HttpUtils.sendGet(url + "/prod-api/system/license/info");
                    String response3 = HttpUtils.sendGet(url + "/prod-api/system/license/simpleInfo");
                    Map<String, Object> licenseInfoMap = (Map<String, Object>) JSON.parseObject(response2, AjaxResult.class).get(AjaxResult.DATA_TAG);
                    licenseInfoMap.put("simpleInfo", JSON.parseObject(response3, AjaxResult.class).get(AjaxResult.DATA_TAG));
                    map.put("licenseInfo", licenseInfoMap);
                    return map;
                });
                executorService.shutdown();
                Map<String, Object> map = future.get(5, TimeUnit.SECONDS);
                RuoYiConfig config = (RuoYiConfig) map.remove("sysInfo");
                return AjaxResult.success(config).put("extraInfo", map.get("licenseInfo"));
            } else {
                throw new ServiceException("URL为空");
            }
        } catch (ServiceException e) {
            return AjaxResult.success(e.getMessage());
        } catch (TimeoutException e) {
            return AjaxResult.success("探测超时");
        } catch (Exception e) {
            return AjaxResult.success(e.getMessage());
        }
    }

    /**
     * 导出验证授权用户列表
     */
    @PreAuthorize("@ss.hasPermi('license:licenseRecord:export')")
    @Log(title = "验证授权用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLicenseRecord sysLicenseRecord) {
        List<SysLicenseRecord> list = sysLicenseRecordService.selectSysLicenseRecordList(sysLicenseRecord);
        ExcelUtil<SysLicenseRecord> util = new ExcelUtil<SysLicenseRecord>(SysLicenseRecord.class);
        util.exportExcel(response, list, "验证授权用户数据");
    }

    /**
     * 获取验证授权用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('license:licenseRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysLicenseRecordService.selectSysLicenseRecordById(id));
    }

    /**
     * 新增验证授权用户
     */
    @PreAuthorize("@ss.hasPermi('license:licenseRecord:add')")
    @Log(title = "验证授权用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLicenseRecord sysLicenseRecord) {
        return toAjax(sysLicenseRecordService.insertSysLicenseRecord(sysLicenseRecord));
    }

    /**
     * 修改验证授权用户
     */
    @PreAuthorize("@ss.hasPermi('license:licenseRecord:edit')")
    @Log(title = "验证授权用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLicenseRecord sysLicenseRecord) {
        return toAjax(sysLicenseRecordService.updateSysLicenseRecord(sysLicenseRecord));
    }

    /**
     * 删除验证授权用户
     */
    @PreAuthorize("@ss.hasPermi('license:licenseRecord:remove')")
    @Log(title = "验证授权用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysLicenseRecordService.deleteSysLicenseRecordByIds(ids));
    }

    /**
     * 移除验证授权用户授权信息
     */
    @PreAuthorize("@ss.hasPermi('license:licenseRecord:remove')")
    @Log(title = "验证授权用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{ids}")
    public AjaxResult removeLicense(@PathVariable Long[] ids) {
        if (ids.length != 1) {
            throw new ServiceException("请选择单个项目进行操作");
        }
        try {
            SysLicenseRecord record = sysLicenseRecordService.selectSysLicenseRecordById(ids[0]);
            // 置为移除状态
            record.setStatus(LicenseStatus.REMOVE);
            SysLicenseRecord record1 = new SysLicenseRecord();
            record1.setId(record.getId());
            record1.setStatus(LicenseStatus.REMOVE);
            sysLicenseRecordService.updateSysLicenseRecord(record1);
            // 移除注册文件
            String webUrl = record.getWebUrl();
            if (StringUtils.isNotBlank(webUrl)) {
                webUrl = webUrl.endsWith("/") ? webUrl.substring(0, webUrl.length() - 1) : webUrl;
                String sign = AesCbcPKCS5PaddingUtil.encode(String.valueOf(System.currentTimeMillis()), Constants.STORE_PASS);
                String response = HttpUtils.sendPost(webUrl + "/prod-api/system/license/remove", "sign=" + sign);
                AjaxResult result = JSON.parseObject(response, AjaxResult.class);
                if ((int) result.get(AjaxResult.CODE_TAG) == 200) {
                    return result;
                }
                throw new ServiceException(String.valueOf(result.get(AjaxResult.MSG_TAG)));
            }
            throw new ServiceException("目标网站地址未知，未移除远程注册文件");
        } catch (Exception e) {
            log.error("", e);
            throw new ServiceException("License移除失败！" + e.getMessage());
        }
    }

    /**
     * 通过网站地址生成授权文件
     *
     * @param loginCode
     * @param webUrl
     * @return
     */
    @GetMapping(value = "/genLicenseFileByWebUrl")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult genLicenseFileByWebUrl(String loginCode, String webUrl, int type) {
        String resultMsg = "";
        try {
            // 验证目标网站
            if(StringUtils.isBlank(webUrl) || StringUtils.isBlank(webUrl.trim())) {
                throw new ServiceException("获取目标网站信息失败，请确认网站域名是否正确：网站域名无效");
            }
            Pattern p = Pattern.compile("(https?://[^/]+/?).*?");
            Matcher m = p.matcher(webUrl);
            if(!m.matches()) {
                throw new ServiceException("获取目标网站信息失败，请确认网站域名是否正确：网站域名无效");
            }
            webUrl = m.group(1);
            webUrl = webUrl.endsWith("/") ? webUrl.substring(0, webUrl.length() - 1) : webUrl;
            String response = HttpUtils.sendGet(webUrl + "/prod-api/system/license/info");
            log.info("getWebsiteLicenseInfoResponse: {}", response);
            if (StringUtils.isBlank(response)) {
                throw new ServiceException("获取目标网站信息失败，请确认网站域名是否正确：返回空白信息");
            }
            WebsiteLicenseInfo websiteLicenseInfo = JSON.parseObject(response, WebsiteLicenseInfo.class);
            if (websiteLicenseInfo.getCode() != 200) {
                throw new ServiceException("获取目标网站授权信息失败，请确认网站域名是否正确：" + response);
            }
//            // 验证目标网站是否未授权或过期授权
//            Date to = websiteLicenseInfo.getData().getLicenseInfo().getTo();
//            if (to != null && to.after(DateUtils.getNowDate())) {
//                throw new ServiceException("目标网站已存在有效授权，请在已有授权过期后再次尝试");
//            }
            String sn = websiteLicenseInfo.getData().getServerInfo().getSn();
            if (StringUtils.isBlank(sn)) {
                throw new ServiceException("获取目标网站信息失败，请确认网站域名是否正确：获取服务器设备码失败");
            }
            checkLoginCode(loginCode, sn);
            String filePath = RuoYiConfig.getDownloadPath() + "license.lic";
            resultMsg = genLicence(loginCode, sn, webUrl, filePath);
            if (type == 0) {
                return AjaxResult.success("license.lic");
            } else {
                // 获取目标网站版本
                String response3 = HttpUtils.sendGet(webUrl + "/prod-api/common/sysInfo");
                log.info("getWebsiteSysInfoResponse: {}", response);
                if (StringUtils.isBlank(response3)) {
                    throw new ServiceException("获取目标网站版本信息失败，请确认网站域名是否正确：返回空白信息");
                }
                Map<String, Object> websiteSysInfo = JSON.parseObject(response3, HashMap.class);
                long versionNo = Long.parseLong(websiteSysInfo.get("versionNo").toString());
                if (versionNo <= 20220827000000L) {
                    throw new ServiceException("在线授权要求目标网站最低版本为v0.0.9，您当前的网站版本为" + websiteSysInfo.get("version").toString() + "，目标网站版本过低，请升级网站版本或选择【通过设备码】方式授权");
                }
                // 编码授权文件
                File file = new File(filePath);
                String license = Base64.encodeBase64URLSafeString(FileUtils.readFileToByteArray(file));
                log.info("sent: " + license);
                log.info("sent: " + MD5.create().digestHex(file));
                String response2 = HttpUtils.sendPost(webUrl + "/prod-api/system/license/inject", "license=" + license);
                log.info("getWebsiteLicenseInjectResponse: {}", response2);
                if (StringUtils.isBlank(response2)) {
                    throw new ServiceException("获取目标网站信息失败，请确认网站域名是否正确：授权时返回空白信息");
                }
                AjaxResult result = JSON.parseObject(response2, AjaxResult.class);
                if ((int) result.get(AjaxResult.CODE_TAG) == 200) {
                    result.put(AjaxResult.MSG_TAG, resultMsg + result.get(AjaxResult.MSG_TAG));
                    return result;
                }
                throw new ServiceException(resultMsg + result.get(AjaxResult.MSG_TAG));
            }
        } catch (Exception e) {
            log.error("", e);
            throw new ServiceException("License生成失败！" + resultMsg + e.getMessage());
        }
    }

    /**
     * 通过网站地址生成授权文件
     *
     * @param loginCode
     * @param deviceCode
     * @return
     */
    @GetMapping(value = "/genLicenseFileByDeviceCode")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult genLicenseFileByDeviceCode(String loginCode, String deviceCode) {
        try {
            checkLoginCode(loginCode, deviceCode);
            // 检查设备码格式
            String regex = "(.{4})(-.{4}){7}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(deviceCode);
            if (deviceCode.length() != 39 || !matcher.matches()) {
                return AjaxResult.error("License生成失败！设备码格式有误，设备码格式如：xxxx-xxxx-xxxx-xxxx-xxxx-xxxx-xxxx-xxxx");
            }
            String filePath = RuoYiConfig.getDownloadPath() + "license.lic";
            genLicence(loginCode, deviceCode, null, filePath);
            return AjaxResult.success("license.lic");
        } catch (Exception e) {
            log.error("", e);
            throw new ServiceException("License生成失败！" + e.getMessage());
        }
    }

    private void checkLoginCode(String loginCode, String deviceCode) {
        // 检查兑换码是否使用过
        SysLoginCode code = sysLoginCodeService.selectSysLoginCodeByCardNo(loginCode);
        if (code == null) {
            throw new ServiceException("授权码无效，请重新核对后重试");
        }
        SysLicenseRecord record = sysLicenseRecordService.selectSysLicenseRecordByLoginCode(loginCode);
        if(record == null) {
            if (UserConstants.YES.equals(code.getIsCharged())) {
                throw new ServiceException("授权码已使用(无授权记录)");
            }
        } else {
            if(!record.getDeviceCode().equals(deviceCode)) {
                throw new ServiceException("授权码已使用(已有授权与当前网站不符)");
            }
        }
    }

    private String genLicence(String loginCode, String sn, String webUrl, String filePath) throws Exception {
        String format = "yyyy/MM/dd HH:mm:ss";
        String returnMsg = "";
        // 调用登录接口
        SysApp app = sysAppService.selectSysAppByAppKey(appKey);
        SysAppVersion version = sysAppVersionService.selectSysAppVersionByAppIdAndVersion(app.getAppId(), versionNo);

        // 根据SN判断网站是否已存在授权
        SysLicenseRecord record = sysLicenseRecordService.selectSysLicenseRecordByDeviceCode(sn);
        boolean flagIsNew = false;
        if(record == null) { // 未授权
            returnMsg = "操作类型：<b>新增授权</b><br><br>";
            loginService.appLogin(loginCode, app, version, null, false);

            record = new SysLicenseRecord();
            record.setDeviceCode(sn);
            record.setLoginCode(loginCode);
            record.setWebUrl(webUrl);
            record.setContact(null);
            record.setName(null);
            sysLicenseRecordService.insertSysLicenseRecord(record);
            flagIsNew = true;
        } else {
            if(Objects.equals(record.getLoginCode(), loginCode)) { // 恢复授权
                returnMsg = "操作类型：<b>恢复授权</b><br><br>";
                returnMsg += "原授权时间：" + DateUtils.parseDateToStr(format, record.getCreateTime()) + " - " + DateUtils.parseDateToStr(format, record.getEndTime()) + "<br>";
            } else { // 续费授权
                returnMsg = "操作类型：<b>续费授权</b><br><br>";
                returnMsg += "原授权时间：" + DateUtils.parseDateToStr(format, record.getCreateTime()) + " - " + DateUtils.parseDateToStr(format, record.getEndTime())+ "<br>";
                Function func = new RechargeLoginCode();
                func.setApp(app);
                Map<String, String> params = new HashMap<>();
                params.put("loginCode", record.getLoginCode());
                params.put("newLoginCode", loginCode);
                func.setParams(params);
                func.handle();
                loginCode = record.getLoginCode();
            }
        }
        // 获取新用户信息
        SysAppUser appUser = sysAppUserService.selectSysAppUserByAppIdAndLoginCode(app.getAppId(), loginCode);

        if(appUser != null) {
            // 生成授权文件
            LicenseCreatorParam param = new LicenseCreatorParam();
            File licenseFile = new File(filePath);
            if (!licenseFile.getParentFile().exists()) {
                licenseFile.getParentFile().mkdirs();
            }
            // 固定参数
            param.setLicensePath(licenseFile.getCanonicalPath());
            param.setSubject(subject);
            param.setPrivateAlias("privateKey");
            param.setKeyPass(Constants.STORE_PASS);
            param.setStorePass(Constants.STORE_PASS);
            param.setPrivateKeysStorePath(PathUtils.getResourceFile("privateKeys.keystore").getCanonicalPath());
            // 自定义参数
            if(appUser.getCreateTime() == null) {
                appUser.setCreateTime(DateUtils.getNowDate());
            }
            param.setIssuedTime(appUser.getCreateTime());
            param.setExpiryTime(appUser.getExpireTime());
            param.setDescription("官方正式授权");
            LicenseCheckModel model = new LicenseCheckModel();
            model.setLicenseTo(webUrl);
            model.setSn(sn);
            model.setLicenseType("全功能版");
            model.setAppLimit(-1);
            model.setMaxOnline(-1);
            model.setIpAddress(new ArrayList<>(Arrays.asList("*")));
            model.setDomainName(new ArrayList<>(Arrays.asList("*")));
            model.setModuleName(new ArrayList<>(Arrays.asList("*")));
            param.setLicenseCheckModel(model);
            LicenseCreator licenseCreator = new LicenseCreator(param);
            licenseCreator.generateLicense();
            // 记录被授权用户
            if (flagIsNew) {
                record.setStartTime(appUser.getCreateTime());
            }
            record.setEndTime(appUser.getExpireTime());
            returnMsg += "新授权时间：" + DateUtils.parseDateToStr(format, record.getStartTime()) + " - " + DateUtils.parseDateToStr(format, record.getEndTime()) + "<br><br>";
            sysLicenseRecordService.updateSysLicenseRecord(record);
            return returnMsg;
        } else {
            return "软件用户生成失败";
        }
    }

}
