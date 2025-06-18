package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.adminAdvertRound.*;
import com.ruoyi.web.controller.xkt.vo.advertRound.AdRoundUpdateVO;
import com.ruoyi.xkt.dto.adminAdvertRound.*;
import com.ruoyi.xkt.dto.advertRound.AdRoundUpdateDTO;
import com.ruoyi.xkt.service.IAdminAdvertRoundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理员管理推广营销轮次投放Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "管理员管理推广营销列表")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/ad-rounds/admin")
public class AdminAdvertRoundController extends XktBaseController {

    final IAdminAdvertRoundService adminAdvertRoundService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "管理员推广营销列表待投放/已投放数量", httpMethod = "GET", response = R.class)
    @GetMapping("/status/count")
    public R<AdminAdRoundStatusCountResVO> statusCount() {
        return R.ok(BeanUtil.toBean(adminAdvertRoundService.statusCount(), AdminAdRoundStatusCountResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "管理员获取推广营销列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<AdminAdRoundPageResDTO>> page(@Validated @RequestBody AdminAdRoundPageVO pageVO) {
        return R.ok(adminAdvertRoundService.page(BeanUtil.toBean(pageVO, AdminAdRoundPageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "管理员审核档口推广图", httpMethod = "PUT", response = R.class)
    @Log(title = "管理员审核档口推广图", businessType = BusinessType.UPDATE)
    @PutMapping("/audit-pic")
    public R<Integer> auditPic(@Validated @RequestBody AdminAdRoundAuditVO auditVO) {
        return R.ok(adminAdvertRoundService.auditPic(BeanUtil.toBean(auditVO, AdminAdRoundAuditDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "管理员退订", httpMethod = "PUT", response = R.class)
    @Log(title = "管理员退订", businessType = BusinessType.UPDATE)
    @PutMapping("/unsubscribe")
    public R<Integer> unsubscribe(@Validated @RequestBody AdminAdRoundUnsubscribeVO unsubscribeVO) {
        return R.ok(adminAdvertRoundService.unsubscribe(BeanUtil.toBean(unsubscribeVO, AdminAdRoundUnsubscribeDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "管理员上传档口推广图", httpMethod = "PUT", response = R.class)
    @Log(title = "管理员上传档口推广图", businessType = BusinessType.UPDATE)
    @PutMapping("/upload/pic")
    public R<Integer> uploadAdvertPic(@Valid @RequestBody AdRoundUpdateVO uploadPicVO) {
        return R.ok(adminAdvertRoundService.uploadAdvertPic(BeanUtil.toBean(uploadPicVO, AdRoundUpdateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "系统拦截广告位", httpMethod = "PUT", response = R.class)
    @Log(title = "系统拦截广告位", businessType = BusinessType.UPDATE)
    @PutMapping("/intercept")
    public R<Integer> sysIntercept(@Valid @RequestBody AdminAdRoundSysInterceptVO interceptVO) {
        return R.ok(adminAdvertRoundService.sysIntercept(BeanUtil.toBean(interceptVO, AdminAdRoundSysInterceptDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "取消拦截广告位", httpMethod = "PUT", response = R.class)
    @Log(title = "取消拦截广告位", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel-intercept")
    public R<Integer> cancelIntercept(@Valid @RequestBody AdminAdRoundCancelInterceptVO cancelInterceptVO) {
        return R.ok(adminAdvertRoundService.cancelIntercept(BeanUtil.toBean(cancelInterceptVO, AdminAdRoundCancelInterceptDTO.class)));
    }


}
