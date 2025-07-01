package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeMember.StoreMemberCreateVO;
import com.ruoyi.xkt.dto.storeMember.StoreMemberCreateDTO;
import com.ruoyi.xkt.service.IStoreMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 档口购买会员controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口购买会员")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-members")
public class StoreMemberController extends XktBaseController {

    final IStoreMemberService storeMemberService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store'||@ss.hasSupplierSubRole())")
    @ApiOperation(value = "新增档口会员", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口会员", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody StoreMemberCreateVO createVO) {
        return R.ok(storeMemberService.create(BeanUtil.toBean(createVO, StoreMemberCreateDTO.class)));
    }

    // TODO 每天获取档口会员过期提醒
    // TODO 每天获取档口会员过期提醒


}
