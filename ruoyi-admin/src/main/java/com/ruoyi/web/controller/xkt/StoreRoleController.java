package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeRole.StoreRolePageVO;
import com.ruoyi.web.controller.xkt.vo.storeRole.StoreRoleUpdateStatusVO;
import com.ruoyi.web.controller.xkt.vo.storeRole.StoreRoleVO;
import com.ruoyi.xkt.dto.storeRole.StoreRoleDTO;
import com.ruoyi.xkt.dto.storeRole.StoreRolePageDTO;
import com.ruoyi.xkt.dto.storeRole.StoreRoleResDTO;
import com.ruoyi.xkt.dto.storeRole.StoreRoleUpdateStatusDTO;
import com.ruoyi.xkt.service.IStoreRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 档口子角色Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口子角色")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/store-roles")
public class StoreRoleController extends XktBaseController {

    final IStoreRoleService storeRoleService;

    /**
     * 新增档口子角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @ApiOperation(value = "新增档口子角色", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口子角色", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody StoreRoleVO storeRoleVO) {
        return R.ok(storeRoleService.insertStoreRole(BeanUtil.toBean(storeRoleVO, StoreRoleDTO.class)));
    }


    /**
     * 编辑档口子角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @ApiOperation(value = "编辑档口子角色", httpMethod = "PUT", response = R.class)
    @Log(title = "编辑档口子角色", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreRoleVO storeRoleVO) {
        return R.ok(storeRoleService.update(BeanUtil.toBean(storeRoleVO, StoreRoleDTO.class)));
    }

    /**
     * 获取档口子角色详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @ApiOperation(value = "获取档口子角色详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeRoleId}")
    public R<StoreRoleVO> getInfo(@PathVariable("storeRoleId") Long storeRoleId) {
        return R.ok(BeanUtil.toBean(storeRoleService.selectByStoreRoleId(storeRoleId), StoreRoleVO.class));
    }

    /**
     * 获取档口子角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @ApiOperation(value = "查询档口销售出库列表", httpMethod = "POST", response = R.class)
    @PostMapping("/list")
    public R<List<StoreRoleResDTO>> list(@Validated @RequestBody StoreRolePageVO rolePageVO) {
        return R.ok(storeRoleService.list(BeanUtil.toBean(rolePageVO, StoreRolePageDTO.class)));
    }

    /**
     * 停用/启用档口子角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @ApiOperation(value = "停用/启用档口子角色", httpMethod = "PUT", response = R.class)
    @Log(title = "停用/启用档口子角色", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public R<Integer> updateRoleStatus(@Validated @RequestBody StoreRoleUpdateStatusVO roleUpdateStatusVO) {
        return R.ok(storeRoleService.updateRoleStatus(BeanUtil.toBean(roleUpdateStatusVO, StoreRoleUpdateStatusDTO.class)));
    }

}
