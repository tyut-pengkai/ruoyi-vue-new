package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.UserInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.xkt.vo.storeCertificate.StoreCertResVO;
import com.ruoyi.web.controller.xkt.vo.storeCertificate.StoreCertVO;
import com.ruoyi.xkt.dto.storeCertificate.StoreCertDTO;
import com.ruoyi.xkt.service.IStoreCertificateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 档口认证Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口认证")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-certs")
public class StoreCertificateController extends XktBaseController {

    final IStoreCertificateService storeCertService;
    final ISysUserService userService;
    final TokenService tokenService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "新增档口认证", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口认证", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody StoreCertVO storeCertVO) {
        Integer count = storeCertService.create(BeanUtil.toBean(storeCertVO, StoreCertDTO.class));
        if (count > 0) {
            this.refreshUserCache();
        }
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "获取档口认证详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}")
    public R<StoreCertResVO> getInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeCertService.getInfo(storeId), StoreCertResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "修改档口认证", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口认证", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> update(@Validated @RequestBody StoreCertVO storeCertVO) {
        Integer count = storeCertService.update(BeanUtil.toBean(storeCertVO, StoreCertDTO.class));
        if (count > 0) {
            this.refreshUserCache();
        }
        return R.ok();
    }

    /**
     * 更新缓存
     */
    public void refreshUserCache() {
        // 当前登录用户关联档口：更新关联用户缓存
        LoginUser currentUser = SecurityUtils.getLoginUser();
        UserInfo currentUserInfo = userService.getUserById(SecurityUtils.getUserId());
        currentUser.updateByUser(currentUserInfo);
        tokenService.refreshToken(currentUser);
    }

}
