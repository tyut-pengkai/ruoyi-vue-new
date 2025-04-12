package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.userSubscriptions.UserSubscDeleteVO;
import com.ruoyi.web.controller.xkt.vo.userSubscriptions.UserSubscPageVO;
import com.ruoyi.web.controller.xkt.vo.userSubscriptions.UserSubscVO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscDeleteDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscPageDTO;
import com.ruoyi.xkt.dto.userSubscriptions.UserSubscPageResDTO;
import com.ruoyi.xkt.service.IUserSubscriptionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户关注档口Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "用户关注档口（只有电商卖家可操作）")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/user-subs")
public class UserSubscriptionsController extends XktBaseController {

    final IUserSubscriptionsService userSubscService;

    /**
     * 新增用户关注档口
     */
    @PreAuthorize("@ss.hasPermi('system:subscriptions:add')")
    @ApiOperation(value = "新增用户关注档口", httpMethod = "POST", response = R.class)
    @Log(title = "新增用户关注档口", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody UserSubscVO subscVO) {
        return R.ok(userSubscService.create(BeanUtil.toBean(subscVO, UserSubscDTO.class)));
    }

    /**
     * 用户关注档口列表
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:list')")
    @ApiOperation(value = "用户关注档口列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<UserSubscPageResDTO>> page(@Validated @RequestBody UserSubscPageVO pageVO) {
        return R.ok(userSubscService.page(BeanUtil.toBean(pageVO, UserSubscPageDTO.class)));
    }


    /**
     * 用户取消关注档口
     */
    @PreAuthorize("@ss.hasPermi('system:subscriptions:remove')")
    @ApiOperation(value = "用户取消关注档口", httpMethod = "DELETE", response = R.class)
    @Log(title = "用户取消关注档口", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch")
    public R<Integer> remove(@Validated @RequestBody UserSubscDeleteVO deleteVO) {
        return R.ok(userSubscService.delete(BeanUtil.toBean(deleteVO, UserSubscDeleteDTO.class)));
    }

}
