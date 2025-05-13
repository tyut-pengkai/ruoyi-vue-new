package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.adminAdvertRound.AdminAdRoundAuditVO;
import com.ruoyi.web.controller.xkt.vo.adminAdvertRound.AdminAdRoundPageVO;
import com.ruoyi.web.controller.xkt.vo.adminAdvertRound.AdminAdRoundSysInterceptVO;
import com.ruoyi.web.controller.xkt.vo.adminAdvertRound.AdminAdRoundUnsubscribeVO;
import com.ruoyi.web.controller.xkt.vo.advertRound.AdRoundUploadPicVO;
import com.ruoyi.xkt.dto.adminAdvertRound.*;
import com.ruoyi.xkt.dto.advertRound.AdRoundUploadPicDTO;
import com.ruoyi.xkt.service.IAdminAdvertRoundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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

    /**
     * 管理员获取推广营销列表
     */
    @ApiOperation(value = "管理员获取推广营销列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<AdminAdRoundPageResDTO>> page(@Validated @RequestBody AdminAdRoundPageVO pageVO) {
        return R.ok(adminAdvertRoundService.page(BeanUtil.toBean(pageVO, AdminAdRoundPageDTO.class)));
    }

    /**
     * 管理员审核档口推广图
     */
    @ApiOperation(value = "管理员审核档口推广图", httpMethod = "PUT", response = R.class)
    @Log(title = "管理员审核档口推广图", businessType = BusinessType.UPDATE)
    @PutMapping("/audit-pic")
    public R<Integer> auditPic(@Validated @RequestBody AdminAdRoundAuditVO auditVO) {
        return R.ok(adminAdvertRoundService.auditPic(BeanUtil.toBean(auditVO, AdminAdRoundAuditDTO.class)));
    }

    /**
     * 管理员退订
     */
    @ApiOperation(value = "管理员退订", httpMethod = "PUT", response = R.class)
    @Log(title = "管理员退订", businessType = BusinessType.UPDATE)
    @PutMapping("/unsubscribe")
    public R<Integer> unsubscribe(@Validated @RequestBody AdminAdRoundUnsubscribeVO unsubscribeVO) {
        return R.ok(adminAdvertRoundService.unsubscribe(BeanUtil.toBean(unsubscribeVO, AdminAdRoundUnsubscribeDTO.class)));
    }

    /**
     * 档口上传推广图
     */
    @ApiOperation(value = "档口上传推广图", httpMethod = "PUT", response = R.class)
    @Log(title = "管理员退订", businessType = BusinessType.UPDATE)
    @PutMapping("/upload/pic")
    public R<Integer> uploadAdvertPic(@Valid @RequestBody AdRoundUploadPicVO uploadPicVO) {
        return R.ok(adminAdvertRoundService.uploadAdvertPic(BeanUtil.toBean(uploadPicVO, AdRoundUploadPicDTO.class)));
    }

    /**
     * 系统拦截广告位
     */
    @ApiOperation(value = "系统拦截广告位", httpMethod = "PUT", response = R.class)
    @Log(title = "系统拦截广告位", businessType = BusinessType.UPDATE)
    @PutMapping("/intercept")
    public R<Integer> sysIntercept(@Valid @RequestBody AdminAdRoundSysInterceptVO interceptVO) {
        return R.ok(adminAdvertRoundService.sysIntercept(BeanUtil.toBean(interceptVO, AdminAdRoundSysInterceptDTO.class)));
    }

    /**
     * 取消拦截广告位
     */
    @ApiOperation(value = "取消拦截广告位", httpMethod = "PUT", response = R.class)
    @Log(title = "取消拦截广告位", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel-intercept/{advertRoundId}")
    public R<Integer> cancelIntercept(@PathVariable Long advertRoundId) {
        return R.ok(adminAdvertRoundService.cancelIntercept(advertRoundId));
    }


}
