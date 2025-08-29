package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.core.page.PageVO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.system.vo.*;
import com.ruoyi.web.controller.xkt.vo.PhoneNumberVO;
import com.ruoyi.web.controller.xkt.vo.UsernameVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@Api(tags = "档口用户")
@RestController
@RequestMapping("/rest/v1/store/user")
public class StoreUserController extends BaseController {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SysLoginService loginService;

    @PreAuthorize("@ss.hasAnyRoles('store')")
    @ApiOperation(value = "用户分页查询 - 档口")
    @PostMapping("/page")
    public R<PageVO<UserListItemVO>> pageByStore(@Validated @RequestBody UserQueryVO vo) {
        Long storeId = SecurityUtils.getStoreId();
        Assert.notNull(storeId);
        UserQuery query = BeanUtil.toBean(vo, UserQuery.class);
        // 只能查询当前档口
        query.setStoreIds(Collections.singletonList(storeId));
        Page<UserListItem> page = PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        userService.listUser(query);
        return R.ok(PageVO.of(page, UserListItemVO.class));
    }


    @PreAuthorize("@ss.hasAnyRoles('store')")
    @ApiOperation(value = "用户详情 - 档口")
    @GetMapping(value = "/{id}")
    public R<UserInfoVO> getInfoByStore(@PathVariable("id") Long id) {
        Long storeId = SecurityUtils.getStoreId();
        Assert.notNull(storeId);
        UserInfo infoDTO = userService.getUserById(id);
        boolean access = CollUtil.emptyIfNull(infoDTO.getRoles())
                .stream()
                .anyMatch(o -> Objects.equals(o.getStoreId(), storeId));
        if (!access) {
            return R.ok();
        }
        Set<Long> subRoleIds = roleService.getSubRoleIdsByStore(storeId);
        UserInfoVO vo = BeanUtil.toBean(infoDTO, UserInfoVO.class);
        // 只展示当前档口角色
        vo.setRoles(CollUtil.emptyIfNull(vo.getRoles())
                .stream()
                .filter(r -> subRoleIds.contains(r.getRoleId()))
                .collect(Collectors.toList()));
        vo.setRoleIds(vo.getRoles().stream().map(RoleInfoVO::getRoleId).collect(Collectors.toList()));
        return R.ok(vo);
    }


    @PreAuthorize("@ss.hasAnyRoles('store')")
    @ApiOperation(value = "发送子账号创建短信验证码 - 档口")
    @PostMapping("/sendSmsVerificationCode")
    public R sendSmsVerificationCode(@Validated @RequestBody PhoneNumberVO vo) {
        loginService.sendSmsVerificationCode(vo.getPhoneNumber(),
                CacheConstants.SMS_REGISTER_CAPTCHA_CODE_CD_PHONE_NUM_KEY, false, null, null);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('store')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @ApiOperation("创建用户 - 档口")
    @PostMapping("/create")
    public R<Long> createByStore(@Valid @RequestBody UserInfoEditByStoreVO vo) {
        Long storeId = SecurityUtils.getStoreId();
        Assert.notNull(storeId);
        Assert.notEmpty(vo.getPhonenumber(), "手机号不能为空");
        Assert.notEmpty(vo.getNickName(), "用户昵称不能为空");
        //短信验证码
        loginService.validateSmsVerificationCode(vo.getPhonenumber(), vo.getCode());
        UserInfoEdit dto = BeanUtil.toBean(vo, UserInfoEdit.class);
        dto.setUserId(null);
        //账号默认手机号
        dto.setUserName(dto.getPhonenumber());
        Set<Long> subRoleIds = roleService.getSubRoleIdsByStore(storeId);
        if (CollUtil.isEmpty(dto.getRoleIds())) {
            dto.setRoleIds(Collections.singletonList(ESystemRole.SELLER.getId()));
        } else {
            dto.getRoleIds().forEach(roleId -> Assert.isTrue(subRoleIds.contains(roleId), "角色非法"));
            dto.getRoleIds().add(ESystemRole.SELLER.getId());
        }
        Long userId = userService.createUser(dto);
        return R.ok(userId);
    }


    @PreAuthorize("@ss.hasAnyRoles('store')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改用户 - 档口")
    @PostMapping("/edit")
    public R<Long> editByStore(@Valid @RequestBody UserInfoEditByStoreVO vo) {
        Long storeId = SecurityUtils.getStoreId();
        Assert.notNull(storeId);
        Assert.notEmpty(vo.getPhonenumber(), "用户手机号不能为空");
        Set<Long> subRoleIds = roleService.getSubRoleIdsByStore(storeId);
        CollUtil.emptyIfNull(vo.getRoleIds())
                .forEach(roleId -> Assert.isTrue(subRoleIds.contains(roleId), "角色非法"));
        UserInfo info = userService.getUserByPhoneNumber(vo.getPhonenumber());
        Assert.notNull(info, "用户不存在");
        Set<Long> roleIds = new HashSet<>();
        List<Long> csRoleIds = new ArrayList<>();
        for (RoleInfo roleInfo : CollUtil.emptyIfNull(info.getRoles())) {
            if (subRoleIds.contains(roleInfo.getRoleId())) {
                csRoleIds.add(roleInfo.getRoleId());
            } else {
                roleIds.add(roleInfo.getRoleId());
            }
        }
        if (csRoleIds.isEmpty()) {
            Assert.notEmpty(vo.getCode(), "验证码不能为空");
            //原来不是当前档口子账号，校验短信验证码
            loginService.validateSmsVerificationCode(vo.getPhonenumber(), vo.getCode());
        }
        UserInfoEdit dto = BeanUtil.toBean(info, UserInfoEdit.class);
        roleIds.addAll(CollUtil.emptyIfNull(vo.getRoleIds()));
        dto.setRoleIds(new ArrayList<>(roleIds));
        dto.setNickName(vo.getNickName());
        Long userId = userService.updateUser(dto);
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(userId);
        return R.ok(userId);
    }


    @PreAuthorize("@ss.hasAnyRoles('store')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改用户状态 - 档口")
    @PostMapping("/changeStatus")
    public R<Integer> changeStatusByStore(@Validated @RequestBody BatchOptStatusVO vo) {
        Long storeId = SecurityUtils.getStoreId();
        Assert.notNull(storeId);
        Assert.isTrue(vo.getIds().size() == 1, "档口不支持同时修改多个用户的状态");
        UserInfo info = userService.getUserById(vo.getIds().get(0));
        Set<Long> subRoleIds = roleService.getSubRoleIdsByStore(storeId);
        boolean accessOpt = CollUtil.emptyIfNull(info.getRoles())
                .stream()
                .anyMatch(roleInfo -> subRoleIds.contains(roleInfo.getRoleId()));
        Assert.isTrue(accessOpt, "当前角色无权修改用户状态");
        int count = userService.batchUpdateUserStatus(vo.getIds(), vo.getStatus());
        if (!Constants.SYS_NORMAL_STATUS.equals(vo.getStatus())) {
            // 清除用户缓存（退出登录）
            tokenService.deleteCacheUser(vo.getIds());
        }
        return R.ok(count);
    }


    @ApiOperation(value = "手机号是否已注册")
    @PostMapping("/isPhoneNumberRegistered")
    public R<Boolean> isPhoneNumberRegistered(@Validated @RequestBody PhoneNumberVO phoneNumberVO) {
        SysUser u = new SysUser();
        u.setPhonenumber(phoneNumberVO.getPhoneNumber());
        boolean unique = userService.checkPhoneUnique(u);
        return R.ok(!unique);
    }

    @ApiOperation(value = "账号名称是否已注册")
    @PostMapping("/isUsernameRegistered")
    public R<Boolean> isUsernameRegistered(@Validated @RequestBody UsernameVO usernameVO) {
        SysUser u = new SysUser();
        u.setUserName(usernameVO.getUserName());
        boolean unique = userService.checkUserNameUnique(u);
        return R.ok(!unique);
    }
}
