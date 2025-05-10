package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.store.*;
import com.ruoyi.xkt.dto.store.*;
import com.ruoyi.xkt.service.IStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 档口Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口功能")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/stores")
public class StoreController extends XktBaseController {

    final IStoreService storeService;

    /**
     * 新增档口
     */
//    // @PreAuthorize("@ss.hasPermi('system:store:edit')")
    @Log(title = "新增档口", businessType = BusinessType.UPDATE)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody StoreCreateVO createVO) {
        return R.ok(storeService.create(BeanUtil.toBean(createVO, StoreCreateDTO.class)));
    }

    /**
     * 修改档口基本信息
     */
//    // @PreAuthorize("@ss.hasPermi('system:store:edit')")
    @ApiOperation(value = "修改档口基本信息", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreUpdateVO storeUpdateVO) {
        return R.ok(storeService.updateStore(BeanUtil.toBean(storeUpdateVO, StoreUpdateDTO.class)));
    }

    /**
     * 查询档口列表
     */
//    // @PreAuthorize("@ss.hasPermi('system:store:list')")
    @ApiOperation(value = "查询档口列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StorePageResDTO>> page(@Validated @RequestBody StorePageVO pageVO) {
        return R.ok(storeService.page(BeanUtil.toBean(pageVO, StorePageDTO.class)));
    }

    /**
     * 档口启用/停用
     */
//    // @PreAuthorize("@ss.hasPermi('system:store:edit')")
    @ApiOperation(value = "档口启用/停用", httpMethod = "PUT", response = R.class)
    @Log(title = "档口启用/停用", businessType = BusinessType.UPDATE)
    @PutMapping("/del-flag")
    public R<Integer> updateDelFlag(@Validated @RequestBody StoreUpdateDelFlagVO updateDelFlagVO) {
        return R.ok(storeService.updateDelFlag(BeanUtil.toBean(updateDelFlagVO, StoreUpdateDelFlagDTO.class)));
    }

    /**
     * 档口审核
     */
//    // @PreAuthorize("@ss.hasPermi('system:store:edit')")
    @ApiOperation(value = "档口审核", httpMethod = "PUT", response = R.class)
    @Log(title = "档口审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public R<Integer> approve(@Validated @RequestBody StoreAuditVO auditVO) {
        return R.ok(storeService.approve(BeanUtil.toBean(auditVO, StoreAuditDTO.class)));
    }

    /**
     * 获取档口详细信息
     */
//    // @PreAuthorize("@ss.hasPermi('system:product:query')")
    @ApiOperation(value = "获取档口详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}")
    public R<StoreBasicResVO> getInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getInfo(storeId), StoreBasicResVO.class));
    }

    /**
     * 档口审核是获取档口基本信息
     */
//    // @PreAuthorize("@ss.hasPermi('system:product:query')")
    @ApiOperation(value = "档口审核是获取档口基本信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/approve/{storeId}")
    public R<StoreApproveResVO> getApproveInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getApproveInfo(storeId), StoreApproveResVO.class));
    }

    /**
     * APP获取档口基本信息
     */
    @ApiOperation(value = "档口审核是获取档口基本信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/app/{storeId}")
    public R<StoreAppResVO> getAppInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getAppInfo(storeId), StoreAppResVO.class));
    }


}
