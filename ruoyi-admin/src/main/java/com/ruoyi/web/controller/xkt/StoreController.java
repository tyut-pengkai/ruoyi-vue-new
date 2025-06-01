package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.oss.OSSClientWrapper;
import com.ruoyi.web.controller.xkt.vo.store.*;
import com.ruoyi.xkt.dto.store.*;
import com.ruoyi.xkt.service.IStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Log(title = "新增档口", businessType = BusinessType.UPDATE)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody StoreCreateVO createVO) {
        return R.ok(storeService.create(BeanUtil.toBean(createVO, StoreCreateDTO.class)));
    }

    @ApiOperation(value = "修改档口基本信息", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreUpdateVO storeUpdateVO) {
        return R.ok(storeService.updateStore(BeanUtil.toBean(storeUpdateVO, StoreUpdateDTO.class)));
    }

    @ApiOperation(value = "查询档口列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StorePageResDTO>> page(@Validated @RequestBody StorePageVO pageVO) {
        return R.ok(storeService.page(BeanUtil.toBean(pageVO, StorePageDTO.class)));
    }

    @ApiOperation(value = "档口启用/停用", httpMethod = "PUT", response = R.class)
    @Log(title = "档口启用/停用", businessType = BusinessType.UPDATE)
    @PutMapping("/del-flag")
    public R<Integer> updateDelFlag(@Validated @RequestBody StoreUpdateDelFlagVO updateDelFlagVO) {
        return R.ok(storeService.updateDelFlag(BeanUtil.toBean(updateDelFlagVO, StoreUpdateDelFlagDTO.class)));
    }

    @ApiOperation(value = "档口审核", httpMethod = "PUT", response = R.class)
    @Log(title = "档口审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public R<Integer> approve(@Validated @RequestBody StoreAuditVO auditVO) {
        return R.ok(storeService.approve(BeanUtil.toBean(auditVO, StoreAuditDTO.class)));
    }

    @ApiOperation(value = "获取档口详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}")
    public R<StoreResVO> getInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getInfo(storeId), StoreResVO.class));
    }

    @ApiOperation(value = "PC 商城首页 获取档口基础信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/simple/{storeId}")
    public R<StoreSimpleResVO> getSimpleInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getSimpleInfo(storeId), StoreSimpleResVO.class));
    }

    @ApiOperation(value = "档口审核是获取档口基本信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/approve/{storeId}")
    public R<StoreApproveResVO> getApproveInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getApproveInfo(storeId), StoreApproveResVO.class));
    }

    @ApiOperation(value = "档口审核是获取档口基本信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/app/{storeId}")
    public R<StoreAppResVO> getAppInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getAppInfo(storeId), StoreAppResVO.class));
    }

    @ApiOperation(value = "管理员审核推广图获取档口联系信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/advert/{storeId}")
    public R<StoreAdvertResVO> getAdvertStoreInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.getAdvertStoreInfo(storeId), StoreAdvertResVO.class));
    }

    @ApiOperation(value = "获取档口首页 数据概览 ", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/index/overview")
    public R<StoreIndexOverviewResVO> indexOverview(@Validated @RequestBody StoreOverviewVO overviewVO) {
        return R.ok(BeanUtil.toBean(storeService.indexOverview(BeanUtil.toBean(overviewVO, StoreOverviewDTO.class)), StoreIndexOverviewResVO.class));
    }

    @ApiOperation(value = "获取档口首页 销售额 ", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/index/sale-revenue")
    public R<List<StoreIndexSaleRevenueResVO>> indexSaleRevenue(@Validated @RequestBody StoreSaleRevenueVO revenueVO) {
        return R.ok(BeanUtil.copyToList(storeService.indexSaleRevenue(BeanUtil.toBean(revenueVO, StoreSaleRevenueDTO.class)), StoreIndexSaleRevenueResVO.class));
    }

    @ApiOperation(value = "获取档口首页 今日销售额 ", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/index/today/sale-revenue/{storeId}")
    public R<StoreIndexTodaySaleResVO> indexTodaySaleRevenue(@PathVariable Long storeId) {
        return R.ok(BeanUtil.toBean(storeService.indexTodaySaleRevenue(storeId), StoreIndexTodaySaleResVO.class));
    }

    @ApiOperation(value = "获取档口首页 商品销售额前10 ", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/index/sale/top10")
    public R<List<StoreIndexSaleTop10ResVO>> indexTop10Sale(@Validated @RequestBody StoreSaleTop10VO saleTop10VO) {
        return R.ok(BeanUtil.copyToList(storeService.indexTop10Sale(BeanUtil.toBean(saleTop10VO, StoreSaleTop10DTO.class)), StoreIndexSaleTop10ResVO.class));
    }

    @ApiOperation(value = "获取档口首页 客户销售榜前10 ", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/index/sale-cus/top10")
    public R<List<StoreIndexCusSaleTop10ResVO>> indexTop10SaleCus(@Validated @RequestBody StoreSaleCustomerTop10VO saleCusTop10VO) {
        return R.ok(BeanUtil.copyToList(storeService.indexTop10SaleCus(BeanUtil.toBean(saleCusTop10VO, StoreSaleCustomerTop10DTO.class)), StoreIndexCusSaleTop10ResVO.class));
    }

    final OSSClientWrapper ossClient;

    @GetMapping("/getKey")
    public R getKey() {
        return R.ok(ossClient.createStsCredentials());
    }

    @PostMapping("/upload")
    public void test(@RequestParam("file") MultipartFile file) throws Exception {
        final String uuid = IdUtil.randomUUID();
        ossClient.upload("advert/" + uuid + ".png", file.getInputStream());
    }

    @GetMapping("/getUrl/{key}/{expireTime}")
    public R getUrl(@PathVariable("key") String key, @PathVariable("expireTime") Long expireTime) throws Exception {
        return R.ok(ossClient.generateUrl(key, expireTime));
    }

}
