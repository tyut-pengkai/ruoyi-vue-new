package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.system.vo.*;
import com.ruoyi.xkt.service.IStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 登录验证
 *
 * @author ruoyi
 */
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

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation(value = "用户名密码登录")
    @PostMapping("/loginByUname")
    public AjaxResult login(@Validated @RequestBody LoginByUsernameVO loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        LoginCredential credential = LoginCredential.builder()
                .loginType(ELoginType.USERNAME)
                .username(loginBody.getUsername())
                .password(loginBody.getPassword())
                .imgUuid(loginBody.getUuid())
                .imgVerificationCode(loginBody.getCode())
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

    @ApiOperation(value = "发送登录短信验证码")
    @PostMapping("/sendSmsVerificationCode")
    public R sendSmsVerificationCode(@Validated @RequestBody LoginSmsReqVO vo) {
        loginService.sendSmsVerificationCode(vo.getPhoneNumber(), vo.getCode(), vo.getUuid());
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
            }
        }
        return vo;
    }
}
