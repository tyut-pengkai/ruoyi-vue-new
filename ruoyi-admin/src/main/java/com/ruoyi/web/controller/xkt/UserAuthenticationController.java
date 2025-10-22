package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.userAuthentication.*;
import com.ruoyi.xkt.dto.userAuthentication.*;
import com.ruoyi.xkt.service.IUserAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 代发认证Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "代发认证")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/user-auths")
public class UserAuthenticationController extends XktBaseController {

    final IUserAuthenticationService userAuthService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,agent')")
    @ApiOperation(value = "新增代发", httpMethod = "POST", response = R.class)
    @Log(title = "新增代发", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Integer> create(@Validated @RequestBody UserAuthCreateVO createVO) {
        return R.ok(userAuthService.create(BeanUtil.toBean(createVO, UserAuthCreateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,agent')")
    @ApiOperation(value = "代发编辑", httpMethod = "PUT", response = R.class)
    @Log(title = "代发编辑", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Integer> update(@Validated @RequestBody UserAuthUpdateVO updateVO) {
        return R.ok(userAuthService.update(BeanUtil.toBean(updateVO, UserAuthUpdateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store,seller,agent')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "APP 代发列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/app/page")
    public R<Page<UserAuthAppPageResDTO>> appPage(@Validated @RequestBody UserAuthPageVO pageVO) {
        return R.ok(userAuthService.appPage(BeanUtil.toBean(pageVO, UserAuthPageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "查询代发列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<UserAuthPageResDTO>> page(@Validated @RequestBody UserAuthPageVO pageVO) {
        return R.ok(userAuthService.page(BeanUtil.toBean(pageVO, UserAuthPageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,agent')")
    @ApiOperation(value = "查询代发详情", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{userAuthId}")
    public R<UserAuthResVO> getInfo(@PathVariable("userAuthId") Long userAuthId) {
        return R.ok(BeanUtil.toBean(userAuthService.getInfo(userAuthId), UserAuthResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,agent')")
    @ApiOperation(value = "根据userId查询代发详情", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/userid/{userId}")
    public R<UserAuthResVO> getInfoByUserId(@PathVariable("userId") Long userId) {
        return R.ok(BeanUtil.toBean(userAuthService.getInfoByUserId(userId), UserAuthResVO.class));
    }


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "代发启用/停用", httpMethod = "PUT", response = R.class)
    @Log(title = "代发启用/停用", businessType = BusinessType.UPDATE)
    @PutMapping("/del-flag")
    public R<Integer> updateDelFlag(@Validated @RequestBody UserAuthUpdateDelFlagVO updateDelFlagVO) {
        return R.ok(userAuthService.updateDelFlag(BeanUtil.toBean(updateDelFlagVO, UserAuthUpdateDelFlagDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "代发审核", httpMethod = "PUT", response = R.class)
    @Log(title = "代发审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public R<Integer> approve(@Validated @RequestBody UserAuthAuditVO auditVO) {
        return R.ok(userAuthService.approve(BeanUtil.toBean(auditVO, UserAuthAuditDTO.class)));
    }

}
