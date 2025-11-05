package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.xkt.vo.store.*;
import com.ruoyi.xkt.dto.store.*;
import com.ruoyi.xkt.service.IStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
    final ISysUserService userService;
    final TokenService tokenService;


    // TODO 获取试用期即将到期的档口
    // TODO 获取试用期即将到期的档口

    // TODO 获取获取正式使用即将到期的档口
    // TODO 获取获取正式使用即将到期的档口


    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "模糊查询档口", httpMethod = "GET", response = R.class)
    @Log(title = "模糊查询档口", businessType = BusinessType.UPDATE)
    @GetMapping("/fuzzy")
    public R<List<StoreNameResVO>> fuzzyQuery(@RequestParam(value = "storeName", required = false) String storeName) {
        return R.ok(BeanUtil.copyToList(storeService.fuzzyQuery(storeName), StoreNameResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "修改档口基本信息", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreUpdateVO storeUpdateVO) {
        return R.ok(storeService.updateStore(BeanUtil.toBean(storeUpdateVO, StoreUpdateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "查询档口列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StorePageResDTO>> page(@Validated @RequestBody StorePageVO pageVO) {
        return R.ok(storeService.page(BeanUtil.toBean(pageVO, StorePageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "档口启用/停用", httpMethod = "PUT", response = R.class)
    @Log(title = "档口启用/停用", businessType = BusinessType.UPDATE)
    @PutMapping("/del-flag")
    public R<Integer> updateDelFlag(@Validated @RequestBody StoreUpdateDelFlagVO updateDelFlagVO) {
        return R.ok(storeService.updateDelFlag(BeanUtil.toBean(updateDelFlagVO, StoreUpdateDelFlagDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "档口审核", httpMethod = "PUT", response = R.class)
    @Log(title = "档口审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public R<Integer> approve(@Validated @RequestBody StoreAuditVO auditVO) {
        return R.ok(storeService.approve(BeanUtil.toBean(auditVO, StoreAuditDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "修改档口权重", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口权重", businessType = BusinessType.UPDATE)
    @PutMapping("/store-weight")
    public R<Integer> updateStoreWeight(@Validated @RequestBody StoreWeightUpdateVO storeWeightUpdateVO) throws IOException {
        return R.ok(storeService.updateStoreWeight(BeanUtil.toBean(storeWeightUpdateVO, StoreWeightUpdateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "获取档口详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}")
    public R<StoreResVO> getInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getInfo(storeId), StoreResVO.class));
    }

    @ApiOperation(value = "档口过期时间", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/expire/{storeId}/{target}")
    public R<StoreExpireResVO> getExpireInfo(@PathVariable("storeId") Long storeId, @PathVariable("target") Integer target) {
        return R.ok(BeanUtil.toBean(storeService.getExpireInfo(storeId, target), StoreExpireResVO.class));
    }


    @ApiOperation(value = "PC 商城首页 获取档口基础信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/simple/{storeId}")
    public R<StoreSimpleResVO> getSimpleInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getSimpleInfo(storeId), StoreSimpleResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "档口审核时获取档口基本信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/approve/{storeId}")
    public R<StoreApproveResVO> getApproveInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getApproveInfo(storeId), StoreApproveResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store,seller,agent')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "APP获取档口基本信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/app/{storeId}")
    public R<StoreAppResVO> getAppInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getAppInfo(storeId), StoreAppResVO.class));
    }

    @ApiOperation(value = "管理员审核推广图获取档口联系信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/advert/{storeId}")
    public R<StoreAdvertResVO> getAdvertStoreInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getAdvertStoreInfo(storeId), StoreAdvertResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "获取档口首页 数据概览 ", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/index/overview")
    public R<StoreIndexOverviewResVO> indexOverview(@Validated @RequestBody StoreOverviewVO overviewVO) {
        return R.ok(BeanUtil.toBean(storeService.indexOverview(BeanUtil.toBean(overviewVO, StoreOverviewDTO.class)), StoreIndexOverviewResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "获取档口首页 按月销售额 ", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/index/month/sale")
    public R<List<StoreIndexSaleRevenueResVO>> indexSaleRevenue(@Validated @RequestBody StoreSaleRevenueVO revenueVO) {
        return R.ok(BeanUtil.copyToList(storeService.indexSaleRevenue(BeanUtil.toBean(revenueVO, StoreSaleRevenueDTO.class)), StoreIndexSaleRevenueResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "获取档口首页 今日商品销售额top ", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/index/today/prod/sale/top/{storeId}")
    public R<StoreIndexTodaySaleTop5ResVO> indexTodayProdSaleRevenueTop(@PathVariable Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.indexTodayProdSaleRevenueTop(storeId, 8), StoreIndexTodaySaleTop5ResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "获取档口首页 今日商品销售额 ", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/index/today/prod/sale/{storeId}")
    public R<List<StoreIndexTodaySaleResVO>> indexTodayProdSaleRevenue(@PathVariable Long storeId) {
        return R.ok(BeanUtil.copyToList(storeService.indexTodayProdSaleRevenue(storeId), StoreIndexTodaySaleResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "获取档口首页 今日客户销售额 ", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/index/today/cus/sale/{storeId}")
    public R<List<StoreIndexTodayCusSaleResVO>> indexTodayCusSaleRevenue(@PathVariable Long storeId) {
        return R.ok(BeanUtil.copyToList(storeService.indexTodayCusSaleRevenue(storeId), StoreIndexTodayCusSaleResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "获取档口首页 商品销售额前10 ", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/index/sale/top10")
    public R<List<StoreIndexSaleTop10ResVO>> indexTop10Sale(@Validated @RequestBody StoreSaleTop10VO saleTop10VO) {
        return R.ok(BeanUtil.copyToList(storeService.indexTop10Sale(BeanUtil.toBean(saleTop10VO, StoreSaleTop10DTO.class)), StoreIndexSaleTop10ResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "获取档口首页 客户销售榜前10 ", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/index/sale-cus/top10")
    public R<List<StoreIndexCusSaleTop10ResVO>> indexTop10SaleCus(@Validated @RequestBody StoreSaleCustomerTop10VO saleCusTop10VO) {
        return R.ok(BeanUtil.copyToList(storeService.indexTop10SaleCus(BeanUtil.toBean(saleCusTop10VO, StoreSaleCustomerTop10DTO.class)), StoreIndexCusSaleTop10ResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "更新档口库存系统", httpMethod = "PUT", response = R.class)
    @PutMapping(value = "/stock-sys")
    public R<Integer> updateStockSys(@Validated @RequestBody StoreUpdateStockSysVO stockSysVO) {
        return R.ok(storeService.updateStockSys(BeanUtil.toBean(stockSysVO, StoreUpdateStockSysDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store,seller,agent')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "APP档口访问榜", httpMethod = "GET", response = R.class)
    @GetMapping("/app/view-rank")
    public R<StoreAppViewRankResVO> getAppViewRank() {
        return R.ok(BeanUtil.toBean(storeService.getAppViewRank(), StoreAppViewRankResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store,seller,agent')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "获取档口库存系统", httpMethod = "GET", response = R.class)
    @GetMapping("/stock-sys/{storeId}")
    public R<Integer> getStockSys(@PathVariable Long storeId) {
        return R.ok(storeService.getStockSys(storeId));
    }

}
