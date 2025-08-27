package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPasswordService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.system.vo.*;
import com.ruoyi.web.controller.xkt.vo.PhoneNumberVO;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.manager.TencentAuthManager;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.service.IStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Slf4j
@Api(tags = "登录")
@RestController
@RequestMapping("/rest/v1/login")
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private IStoreService storeService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TencentAuthManager tencentAuthManager;
    @Autowired
    private SysPasswordService passwordService;
    @Autowired
    private StoreMapper storeMapper;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation(value = "用户名密码登录")
    @PostMapping("/loginByUname")
    public AjaxResult login(@Validated @RequestBody LoginByUsernameVO loginBody) {
        boolean captchaPass = tencentAuthManager.validate(loginBody.getTicket(), loginBody.getRandstr());
        if (!captchaPass) {
            return AjaxResult.error("验证失败");
        }
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        LoginCredential credential = LoginCredential.builder()
                .loginType(ELoginType.USERNAME)
                .username(loginBody.getUsername())
                .password(loginBody.getPassword())
                .build();
        String token = loginService.login(credential);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation(value = "短信验证码登录")
    @PostMapping("/loginBySms")
    public AjaxResult loginBySms(@Validated @RequestBody LoginBySmsCodeVO loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        LoginCredential credential = LoginCredential.builder()
                .loginType(ELoginType.SMS_VERIFICATION_CODE)
                .phoneNumber(loginBody.getPhoneNumber())
                .smsVerificationCode(loginBody.getCode())
                .build();
        String token = loginService.login(credential);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation(value = "发送短信验证码(未登录)")
    @PostMapping("/sendSmsVerificationCode")
    public R sendSmsVerificationCode(@Validated @RequestBody LoginSmsReqVO vo) {
        boolean captchaPass = tencentAuthManager.validate(vo.getTicket(), vo.getRandstr());
        if (!captchaPass) {
            return R.fail("验证失败");
        }
        loginService.sendSmsVerificationCode(vo.getPhoneNumber(),
                CacheConstants.SMS_LOGIN_CAPTCHA_CODE_CD_PHONE_NUM_KEY, false, null, null);
        return R.ok();
    }

    @ApiOperation(value = "发送短信验证码(已登录)")
    @PostMapping("/sendSmsVerificationCodeByUser")
    public R sendSmsVerificationCode(@Validated @RequestBody PhoneNumberVO vo) {
        loginService.sendSmsVerificationCode(vo.getPhoneNumber(),
                CacheConstants.SMS_LOGIN_CAPTCHA_CODE_CD_PHONE_NUM_KEY, false, null, null);
        return R.ok();
    }

    @ApiOperation(value = "扫码登录：WEB端第一步，获取随机ID，使用此ID生成二维码（10分钟有效，超时需要重新获取）")
    @GetMapping("/getBrowserId")
    public R<BrowserIdVO> getBrowserId() {
        String browserId = IdUtil.randomUUID();
        // 10分钟内有效
        redisCache.setCacheObject(CacheConstants.SCAN_CODE_LOGIN_BROWSER_ID_KEY + browserId, "", 10,
                TimeUnit.MINUTES);
        log.info("生成浏览器id: {}", browserId);
        return R.ok(BrowserIdVO.builder().browserId(browserId).build());
    }

    @ApiOperation(value = "扫码登录：WEB端第二步，使用第一步获取到的ID轮询接口，直到成功获取到token或ID超时失效重新获取ID")
    @PostMapping("/getTokenByBrowserId")
    public AjaxResult getTokenByBrowserId(@Validated @RequestBody BrowserIdVO vo) {
        AjaxResult ajax = AjaxResult.success();
        String token = redisCache.getCacheObject(CacheConstants.SCAN_CODE_LOGIN_BROWSER_ID_KEY + vo.getBrowserId());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation(value = "扫码登录：APP第一步，扫描WEB二维码获取ID；APP第二步，使用ID完成登录")
    @PostMapping("/loginByBrowserId")
    public R loginByBrowserId(@Validated @RequestBody BrowserIdVO vo) {
        String cacheKey = CacheConstants.SCAN_CODE_LOGIN_BROWSER_ID_KEY + vo.getBrowserId();
        String cacheValue = redisCache.getCacheObject(cacheKey);
        if (cacheValue == null) {
            throw new ServiceException("二维码已过期");
        }
        LoginUser appLoginUser = SecurityUtils.getLoginUser();
        UserInfo user = userService.getUserById(appLoginUser.getUserId());
        LoginUser webLoginUser = new LoginUser(user);
        String token = loginService.createToken(webLoginUser);
        redisCache.setCacheObject(cacheKey, token, 10, TimeUnit.MINUTES);
        return R.ok();
    }

    @ApiOperation(value = "注销登录（退出登录）")
    @GetMapping("/logout")
    public R logout() {
        return R.ok();
    }


    @Log(title = "删除账号", businessType = BusinessType.DELETE)
    @ApiOperation(value = "注销账号（删除当前登录账号）")
    @PostMapping("/user/remove")
    public R removeCurrentUser(@Validated @RequestBody LoginBySmsCodeVO vo) {
        loginService.validateSmsVerificationCode(vo.getPhoneNumber(), vo.getCode());
        UserExt currentUser = SecurityUtils.getLoginUser().getUser();
        Assert.notNull(currentUser);
        String phoneNumber = currentUser.getPhonenumber();
        Assert.isTrue(StrUtil.equals(vo.getPhoneNumber(), phoneNumber), "手机号与账号不匹配");
        userService.batchDeleteUser(Collections.singletonList(currentUser.getUserId()));
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(Collections.singletonList(currentUser.getUserId()));
        return R.ok();
    }

    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改密码（使用手机验证码修改）")
    @PostMapping("/changePassword")
    public R changePassword(@Validated @RequestBody PasswordChangeVO vo) {
        loginService.validateSmsVerificationCode(vo.getPhoneNumber(), vo.getCode());
        UserInfo user = userService.getUserByPhoneNumber(vo.getPhoneNumber());
        String username = userService.resetPassword(user.getUserId(), vo.getNewPassword());
        passwordService.clearLoginRecordCache(username);
        tokenService.deleteCacheUser(user.getUserId());
        return R.ok();
    }

    @Log(title = "修改密码", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改密码（使用原密码修改）")
    @PostMapping("/changePassword2")
    public R changePassword2(@Validated @RequestBody PasswordChange2VO vo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(password, vo.getOldPassword())) {
            return R.fail("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(password, vo.getNewPassword())) {
            return R.fail("新密码不能与旧密码相同");
        }
        String username = userService.resetPassword(loginUser.getUserId(), vo.getNewPassword());
        passwordService.clearLoginRecordCache(username);
        tokenService.deleteCacheUser(loginUser.getUserId());
        return R.ok();
    }

    @Log(title = "修改头像", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改头像")
    @PostMapping("/changeAvatar")
    public R changeAvatar(@Validated @RequestBody AvatarChangeVO vo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        boolean success = userService.updateUserAvatar(loginUser.getUsername(), vo.getAvatar());
        if (success) {
            return R.ok();
        }
        return R.fail();
    }

    @Log(title = "修改手机号", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改手机号")
    @PostMapping("/changePhoneNumber")
    public R changePhoneNumber(@Validated @RequestBody LoginBySmsCodeVO vo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String old = loginUser.getUser().getPhonenumber();
        String now = vo.getPhoneNumber();
        Assert.isFalse(StrUtil.equals(old, now), "新手机号不能与原手机号相同");
        loginService.validateSmsVerificationCode(vo.getPhoneNumber(), vo.getCode());
        userService.updateUserPhoneNumber(loginUser.getUserId(), vo.getPhoneNumber());
        tokenService.deleteCacheUser(loginUser.getUserId());
        return R.ok();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation(value = "当前登录用户")
    @GetMapping("/user/current")
    public R<UserLoginInfoVO> currentUser() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserInfo infoDTO = userService.getUserById(loginUser.getUserId());
        return R.ok(trans2UserLoginInfoVO(loginUser, infoDTO));
    }

    @ApiOperation(value = "可选择的角色列表")
    @GetMapping("/role/list")
    public R<List<RoleSelectItemVO>> listRoleSelectItem() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<RoleSelectItem> items = roleService.listRoleSelectItem(loginUser.getUserId());
        Set<Long> relStoreIds = items.stream().map(RoleSelectItem::getRelStoreId).filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> storeNameMap = storeService.getStoreNameByIds(relStoreIds);
        List<RoleSelectItemVO> vos = new ArrayList<>(items.size());
        Long currentRoleId = loginUser.getCurrentRoleId();
        Long currentStoreId = loginUser.getCurrentStoreId();
        for (RoleSelectItem item : items) {
            RoleSelectItemVO vo = BeanUtil.toBean(item, RoleSelectItemVO.class);
            vos.add(vo);
            vo.setRelStoreName(storeNameMap.get(vo.getRelStoreId()));
            vo.setCurrent(Objects.equals(currentRoleId, vo.getRoleId())
                    && Objects.equals(currentStoreId, vo.getRelStoreId()));
        }
        return R.ok(vos);
    }

    @ApiOperation(value = "切换角色")
    @PostMapping("/role/change")
    public R<UserLoginInfoVO> changeRole(@Validated @RequestBody RoleSelectReqVO vo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserInfo infoDTO = userService.getUserById(loginUser.getUserId());
        for (RoleInfo roleInfo : CollUtil.emptyIfNull(infoDTO.getRoles())) {
            if (roleInfo.getRoleId().equals(vo.getRoleId())
                    && roleInfo.getRelStoreId().equals(vo.getRelStoreId())) {
                //切换
                loginUser.setCurrentRoleId(roleInfo.getRoleId());
                loginUser.setCurrentStoreId(roleInfo.getRelStoreId());
                loginUser.setUser(UserExt.create(infoDTO));
                tokenService.refreshToken(loginUser);
                break;
            }
        }
        return R.ok(trans2UserLoginInfoVO(loginUser, infoDTO));
    }

    /**
     * 组装登录用户信息
     *
     * @param loginUser
     * @param userInfo
     * @return
     */
    private UserLoginInfoVO trans2UserLoginInfoVO(LoginUser loginUser, Object userInfo) {
        UserLoginInfoVO vo = BeanUtil.toBean(userInfo, UserLoginInfoVO.class);
        List<Long> roleIds = new ArrayList<>();
        vo.setRoleIds(roleIds);
        Long currentRoleId = loginUser.getCurrentRoleId();
        Long currentStoreId = loginUser.getCurrentStoreId();
        for (RoleInfoVO roleInfoVO : CollUtil.emptyIfNull(vo.getRoles())) {
            roleIds.add(roleInfoVO.getRoleId());
            if (Objects.equals(currentRoleId, roleInfoVO.getRoleId())
                    && Objects.equals(currentStoreId, roleInfoVO.getRelStoreId())) {
                //当前角色
                vo.setCurrentRole(roleInfoVO);
                //当前角色菜单
                List<SysMenu> currentMenus = new ArrayList<>(CollUtil.size(roleInfoVO.getMenus()));
                List<Long> currentMenuIds = new ArrayList<>(currentMenus.size());
                for (MenuInfoVO menuInfoVO : CollUtil.emptyIfNull(roleInfoVO.getMenus())) {
                    currentMenuIds.add(menuInfoVO.getMenuId());
                    currentMenus.add(BeanUtil.toBean(menuInfoVO, SysMenu.class));
                }
                vo.setCurrentMenuIds(currentMenuIds);
                vo.setCurrentMenuTreeNodes(BeanUtil.copyToList(menuService.getMenuTree(currentMenus),
                        MenuTreeNodeVO.class));
                //当前档口
                Long storeId = roleInfoVO.getRelStoreId();
                vo.setCurrentStoreId(storeId);
                if (storeId != null) {
                    Store store = this.storeMapper.selectById(storeId);
                    vo.setCurrentStoreName(ObjectUtils.isNotEmpty(store) ? store.getStoreName() : "");
                    //档口状态
                    vo.setCurrentStoreStatus(ObjectUtils.isNotEmpty(store) ? store.getStoreStatus() : null);
                }
            }
        }
        return vo;
    }
}
