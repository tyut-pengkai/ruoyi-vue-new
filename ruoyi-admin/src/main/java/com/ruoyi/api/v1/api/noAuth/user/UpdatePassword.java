package com.ruoyi.api.v1.api.noAuth.user;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;

import javax.annotation.Resource;

public class UpdatePassword extends Function {

    @Resource
    private ISysUserService userService;

    @Override
    public void init() {
        this.setApi(new Api("updatePassword.nu", "修改账号密码", false, Constants.API_TAG_ACCOUNT,
                "修改账号密码", new AuthType[]{AuthType.ACCOUNT}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("username", true, "账号"),
                        new Param("password", true, "原密码"),
                        new Param("newPassword", true, "新密码"),
                        new Param("newPasswordRepeat", true, "重复新密码"),
                }, new Resp(Resp.DataType.string, "成功返回0")));
    }

    @Override
    public Object handle() {
        String username = this.getParams().get("username");
        String password = this.getParams().get("password");
        String newPassword = this.getParams().get("newPassword");
        String newPasswordRepeat = this.getParams().get("newPasswordRepeat");

        if (!newPassword.equals(newPasswordRepeat)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_ERROR, "新密码与重复新密码不一致");
        }
        if (password.equals(newPassword)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_ERROR, "新密码不能与原密码相同");
        }
        SysUser user = userService.selectUserByUserName(username);
        if (user != null) {
            if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
                throw new ApiException(ErrorCode.ERROR_USERNAME_OR_PASSWORD_ERROR, "原密码有误");
            }
            String newPasswordEn = SecurityUtils.encryptPassword(newPassword);
            user.setPassword(newPasswordEn);
            userService.updateUser(user);

            return "0";
        } else {
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST);
        }
    }
}
