package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.framework.api.v1.utils.ValidUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.SysGlobalFile;
import com.ruoyi.system.domain.SysWebsite;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysGlobalFileService;
import com.ruoyi.system.service.ISysWebsiteService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class GlobalFileDownload extends Function {

    @Resource
    private ISysGlobalFileService globalFileService;
    @Resource
    private ValidUtils validUtils;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private TokenService tokenService;
    @Value("${swagger.pathMapping}")
    private String pathMapping;
    @Resource
    private ISysWebsiteService sysWebsiteService;
    @Resource
    private ISysConfigService configService;
    @Resource
    private RedisCache redisCache;

    @Override
    public void init() {
        this.setApi(new Api("globalFileDownload.ng", "远程文件下载", false, Constants.API_TAG_GENERAL,
                "远程文件下载", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, new Param[]{
                new Param("fileName", true, "文件名称"),
                new Param("returnUrl", false, "是返回下载链接，否返回文件base64编码文本，是传1否传0默认为0"),
                new Param("errorIfNotExist", false, "当文件不存在时是否报错，如果为否则返回空，是传1否传0默认为0"),
        }, new Resp(Resp.DataType.string, "成功返回文件下载链接或文件base64编码文本，文件不存在返回空")));
    }

    @Override
    public Object handle() {

        String fileName = this.getParams().get("fileName");
        boolean returnUrl = Convert.toBool(this.getParams().get("returnUrl"), false);
        boolean errorIfNotExist = Convert.toBool(this.getParams().get("errorIfNotExist"), false);

        SysGlobalFile globalFile = globalFileService.selectSysGlobalFileByName(fileName);
        if (globalFile == null) {
            if (errorIfNotExist) {
                throw new ApiException(ErrorCode.ERROR_GLOBAL_FILE_NOT_EXIST);
            } else {
                return "";
            }
        }

        // 检查是否登录
        if (UserConstants.YES.equals(globalFile.getCheckToken())) {
            try {
                getLoginUser();
            } catch (Exception e) {
                throw new ApiException(ErrorCode.ERROR_NOT_LOGIN, "下载此文件需要用户登录");
            }
        }
        // 检查是否VIP
        if (UserConstants.YES.equals(globalFile.getCheckVip())) {
            try {
                // 实时从数据库取软件用户状态，避免使用登录时存储的状态造成数据同步不及时
                SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(getLoginUser().getAppUserId());
                validUtils.checkAppUserIsExpired(getApp(), appUser, true);
            } catch (Exception e) {
                throw new ApiException(ErrorCode.ERROR_NOT_VIP, e.getMessage());
            }
        }

        String filePath = RuoYiConfig.getGlobalFilePath() + File.separator + globalFile.getId();
        File file = new File(filePath);
        if (file.exists()) {
            try {
                if (returnUrl) {
                    String randomPath = String.valueOf(new SnowflakeIdWorker().nextId());
                    FileUtils.copyFile(file, new File(RuoYiConfig.getGlobalFilePath() + File.separator + randomPath + File.separator + fileName));
                    // 存入redis。默认有效期1小时
                    String enableTimeStr = configService.selectConfigByKey("sys.api.globalFileUrlEnableTime");
                    int enableTime = Convert.toInt(enableTimeStr, 3600);
                    redisCache.setCacheObject(CacheConstants.GLOBAL_FILE_DOWNLOAD_KEY + randomPath + File.separator + fileName, null, enableTime, TimeUnit.SECONDS);
                    return getUrlPrefix(randomPath) + fileName;
                } else {
                    return Base64Utils.encodeToString(FileUtils.readFileToByteArray(file));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ApiException(ErrorCode.ERROR_OTHER_FAULTS, e.getMessage());
            }
        } else {
            if (errorIfNotExist) {
                throw new ApiException(ErrorCode.ERROR_GLOBAL_FILE_NOT_EXIST);
            } else {
                return "";
            }
        }

    }

    private String getUrlPrefix(String randomPath) {
        String notifyUrl;
        SysWebsite website = sysWebsiteService.getById(1);
        if (website != null && StringUtils.isNotBlank(website.getDomain())) {
            notifyUrl = website.getDomain();
        } else {
            HttpServletRequest request = ServletUtils.getRequest();
            String port = "80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort();
            notifyUrl = request.getScheme() + "://" + request.getServerName() + port;
        }
        if (notifyUrl.endsWith("/")) {
            notifyUrl = notifyUrl.substring(0, notifyUrl.length() - 1);
        }
        notifyUrl += pathMapping + "/common/globalFileDownload/" + randomPath + "/";
        return notifyUrl;
    }
}
