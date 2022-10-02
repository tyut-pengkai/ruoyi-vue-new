package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.framework.api.v1.utils.ValidUtils;
import com.ruoyi.system.domain.SysGlobalFile;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysGlobalFileService;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

public class GlobalFileUpload extends Function {

    @Resource
    private ISysGlobalFileService globalFileService;
    @Resource
    private ValidUtils validUtils;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysConfigService configService;

    @Override
    public void init() {
        this.setApi(new Api("globalFileUpload.ng", "远程文件上传", false, Constants.API_TAG_GENERAL,
                "远程文件上传", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, new Param[]{
                new Param("fileName", true, "文件名称"),
                new Param("base64Str", true, "文件base64编码文本"),
                new Param("overrideIfExist", false, "当文件已存在时是否覆盖，是传1否传0默认为1"),
                new Param("checkToken", false, "文件是否需要登录才能读写，是传1否传0默认为0"),
                new Param("checkVip", false, "文件是否需要VIP才能读写，是传1否传0默认为0"),
        }, new Resp(Resp.DataType.string, "成功返回0")));
    }

    @Override
    public Object handle() {

        if (!Convert.toBool(configService.selectConfigByKey("sys.api.enableFileUpload"))) {
            throw new ApiException(ErrorCode.ERROR_API_NOT_ENABLED);
        }

        String fileName = this.getParams().get("fileName");
        String base64Str = this.getParams().get("base64Str");
        boolean overrideIfExist = Convert.toBool(this.getParams().get("overrideIfExist"), true);
        boolean checkToken = Convert.toBool(this.getParams().get("checkToken"), false);
        boolean checkVip = Convert.toBool(this.getParams().get("checkVip"), false);

        SysGlobalFile globalFile = globalFileService.selectSysGlobalFileByName(fileName);
        if (globalFile == null) {
            globalFile = new SysGlobalFile();
            globalFile.setName(fileName);
            globalFile.setCheckToken(checkToken ? "Y" : "N");
            globalFile.setCheckVip(checkVip ? "Y" : "N");
            globalFileService.insertSysGlobalFile(globalFile);
        } else {
            if (!overrideIfExist) {
                throw new ApiException(ErrorCode.ERROR_GLOBAL_FILE_ALREADY_EXIST);
            }
            // 检查是否登录
            if (UserConstants.YES.equals(globalFile.getCheckToken())) {
                try {
                    getLoginUser();
                } catch (Exception e) {
                    throw new ApiException(ErrorCode.ERROR_NOT_LOGIN, "写此文件需要用户登录");
                }
            }
            // 检查是否VIP
            if (UserConstants.YES.equals(globalFile.getCheckVip())) {
                try {
                    // 实时从数据库取软件用户状态，避免使用登录时存储的状态造成数据同步不及时
                    SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(getLoginUser().getAppUser().getAppUserId());
                    validUtils.checkAppUserIsExpired(getApp(), appUser, true);
                } catch (Exception e) {
                    throw new ApiException(ErrorCode.ERROR_NOT_VIP, e.getMessage());
                }
            }
        }

        try {
            byte[] bytes = Base64Utils.decodeFromString(base64Str);
            String filePath = RuoYiConfig.getGlobalFilePath() + "/" + globalFile.getId();
            File desc = new File(filePath);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            FileUtils.writeByteArrayToFile(desc, bytes);
            globalFileService.updateSysGlobalFile(globalFile);
            return "0";
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_ERROR, "解析文件内容出错");
        }

    }
}
