package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeCusProdDiscount.StoreCusProdBatchDiscountVO;
import com.ruoyi.web.controller.xkt.vo.storeCusProdDiscount.StoreCusProdDiscExistVO;
import com.ruoyi.web.controller.xkt.vo.storeCusProdDiscount.StoreCusProdDiscPageVO;
import com.ruoyi.web.controller.xkt.vo.storeCusProdDiscount.StoreCusProdDiscountVO;
import com.ruoyi.web.controller.xkt.vo.storeProd.StoreProdSkuResVO;
import com.ruoyi.xkt.dto.storeCusProdDiscount.*;
import com.ruoyi.xkt.service.IStoreCustomerProductDiscountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 档口客户优惠Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口客户优惠")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/cus-disc")
public class StoreCustomerProductDiscountController extends XktBaseController {

    final IStoreCustomerProductDiscountService storeCusProdDiscService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "销售出库时，新增或修改档口客户优惠（全店商品优惠）", httpMethod = "PUT", response = R.class)
    @Log(title = "销售出库时，新增或修改档口客户优惠（全店商品优惠）", businessType = BusinessType.UPDATE)
    @PutMapping("/all-prod-discount")
    public R<Integer> allProdDiscount(@Validated @RequestBody StoreCusProdDiscountVO cusProdDisVO) {
        return R.ok(storeCusProdDiscService.updateStoreCusProdDiscount(BeanUtil.toBean(cusProdDisVO, StoreCusProdDiscountDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "客户销售管理 批量减价、批量抹零减价、新增客户定价优惠", httpMethod = "PUT", response = R.class)
    @Log(title = "客户销售管理 批量减价、批量抹零减价、新增客户定价优惠", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public R<Integer> batchDiscount(@Validated @RequestBody StoreCusProdBatchDiscountVO batchDiscVO) {
        return R.ok(storeCusProdDiscService.batchDiscount(BeanUtil.toBean(batchDiscVO, StoreCusProdBatchDiscountDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "查询客户销售管理列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreCusProdDiscPageResDTO>> selectPage(@Validated @RequestBody StoreCusProdDiscPageVO pageVO) {
        return R.ok(storeCusProdDiscService.selectPage(BeanUtil.toBean(pageVO, StoreCusProdDiscPageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "新增客户销售定价时，根据入参查询是否已存在优惠", httpMethod = "POST", response = R.class)
    @PostMapping("/exists")
    public R<List<StoreCusProdDiscExistResDTO>> discountExist(@Validated @RequestBody StoreCusProdDiscExistVO existVO) {
        return R.ok(storeCusProdDiscService.discountExist(BeanUtil.toBean(existVO, StoreCusProdDiscExistDTO.class)));
    }


}
