package com.ruoyi.license.controller;

import com.alibaba.fastjson2.JSON;
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
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.license.bo.LicenseCheckModel;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Value("${license.subject}")
    private String subject;
    /**
     * 授权APP KEY
     */
    @Value("${license.licenseServer.appKey}")
    private String appKey;
    /**
     * 授权APP版本号
     */
    @Value("${license.licenseServer.appVersionNo}")
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
     * 通过网站地址生成授权文件
     *
     * @param loginCode
     * @param webUrl
     * @return
     */
    @GetMapping(value = "/genLicenseFileByWebUrl")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult genLicenseFileByWebUrl(String loginCode, String webUrl) {
        try {
            // 检查兑换码是否使用过
            SysLoginCode code = sysLoginCodeService.selectSysLoginCodeByCardNo(loginCode);
            if (UserConstants.YES.equals(code.getIsCharged())) {
                throw new ServiceException("兑换码已使用");
            }
            // 验证目标网站
            webUrl = webUrl.endsWith("/") ? webUrl.substring(0, webUrl.length() - 1) : webUrl;
            String response = HttpUtils.sendGet(webUrl + "/prod-api/system/license/info");
            log.info("getWebsiteLicenseInfoResponse: {}", response);
            if (StringUtils.isBlank(response)) {
                throw new ServiceException("获取目标网站信息失败，请确认网站域名是否正确：返回空白信息");
            }
            WebsiteLicenseInfo websiteLicenseInfo = JSON.parseObject(response, WebsiteLicenseInfo.class);
            if (websiteLicenseInfo.getCode() != 200) {
                throw new ServiceException("获取目标网站信息失败，请确认网站域名是否正确：" + response);
            }
            String sn = websiteLicenseInfo.getData().getServerInfo().getSn();
            if (StringUtils.isBlank(sn)) {
                throw new ServiceException("获取目标网站信息失败，请确认网站域名是否正确：获取服务器设备码失败");
            }
            // 调用登录接口
            SysApp app = sysAppService.selectSysAppByAppKey(appKey);
            SysAppVersion version = sysAppVersionService.selectSysAppVersionByAppIdAndVersion(app.getAppId(), versionNo);
            loginService.appLogin(loginCode, app, version, null, false);
            // 获取新用户信息
            SysAppUser appUser = sysAppUserService.selectSysAppUserByAppIdAndLoginCode(app.getAppId(), loginCode);
            // 生成授权文件
            LicenseCreatorParam param = new LicenseCreatorParam();
            String filePath = RuoYiConfig.getDownloadPath() + "license.lic";
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
            SysLicenseRecord record = new SysLicenseRecord();
            record.setDeviceCode(sn);
            record.setLoginCode(loginCode);
            record.setWebUrl(webUrl);
            record.setContact(null);
            record.setName(null);
            record.setStartTime(appUser.getCreateTime());
            record.setEndTime(appUser.getExpireTime());
            sysLicenseRecordService.insertSysLicenseRecord(record);

            return AjaxResult.success("license.lic");
        } catch (Exception e) {
            return AjaxResult.error("License生成失败！" + e.getMessage());
        }
    }

}
