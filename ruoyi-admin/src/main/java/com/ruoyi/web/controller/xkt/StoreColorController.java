package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.storeColor.StoreColorVO;
import com.ruoyi.xkt.service.IStoreColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 档口所有颜色Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@Api(tags = "档口所有颜色")
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-colors")
public class StoreColorController extends XktBaseController {

    final IStoreColorService storeColorService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "查询档口颜色列表", httpMethod = "GET", response = R.class)
    @GetMapping("/list/{storeId}")
    public R<List<StoreColorVO>> list(@PathVariable Long storeId) {
        return R.ok(BeanUtil.copyToList(storeColorService.list(storeId), StoreColorVO.class));
    }

}
