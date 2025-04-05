package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeCusProdDiscount.StoreCusProdDiscountVO;
import com.ruoyi.xkt.domain.StoreCustomerProductDiscount;
import com.ruoyi.xkt.dto.storeCusProdDiscount.StoreCusProdDiscountDTO;
import com.ruoyi.xkt.service.IStoreCustomerProductDiscountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/rest/v1/cus-discounts")
public class StoreCustomerProductDiscountController extends XktBaseController {

    final IStoreCustomerProductDiscountService storeCusProdDiscountSvc;

    /**
     * 销售出库时，新增或修改档口客户优惠
     */
    @PreAuthorize("@ss.hasPermi('system:discount:edit')")
    @ApiOperation(value = "销售出库时，新增或修改档口客户优惠", httpMethod = "PUT", response = R.class)
    @Log(title = "销售出库时，新增或修改档口客户优惠", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreCusProdDiscountVO cusProdDisVO) {
        return R.ok(storeCusProdDiscountSvc.updateStoreCusProdDiscount(BeanUtil.toBean(cusProdDisVO, StoreCusProdDiscountDTO.class)));
    }


    /**
     * 查询档口客户优惠列表
     */
    @PreAuthorize("@ss.hasPermi('system:discount:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreCustomerProductDiscount storeCustomerProductDiscount) {
        startPage();
        List<StoreCustomerProductDiscount> list = storeCusProdDiscountSvc.selectStoreCustomerProductDiscountList(storeCustomerProductDiscount);
        return getDataTable(list);
    }

    /**
     * 导出档口客户优惠列表
     */
    @PreAuthorize("@ss.hasPermi('system:discount:export')")
    @Log(title = "档口客户优惠", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreCustomerProductDiscount storeCustomerProductDiscount) {
        List<StoreCustomerProductDiscount> list = storeCusProdDiscountSvc.selectStoreCustomerProductDiscountList(storeCustomerProductDiscount);
        ExcelUtil<StoreCustomerProductDiscount> util = new ExcelUtil<StoreCustomerProductDiscount>(StoreCustomerProductDiscount.class);
        util.exportExcel(response, list, "档口客户优惠数据");
    }

    /**
     * 获取档口客户优惠详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:discount:query')")
    @GetMapping(value = "/{storeCusProdDiscId}")
    public R getInfo(@PathVariable("storeCusProdDiscId") Long storeCusProdDiscId) {
        return success(storeCusProdDiscountSvc.selectStoreCustomerProductDiscountByStoreCusProdDiscId(storeCusProdDiscId));
    }

    /**
     * 新增档口客户优惠
     */
    @PreAuthorize("@ss.hasPermi('system:discount:add')")
    @Log(title = "档口客户优惠", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreCustomerProductDiscount storeCustomerProductDiscount) {
        return success(storeCusProdDiscountSvc.insertStoreCustomerProductDiscount(storeCustomerProductDiscount));
    }


    /**
     * 删除档口客户优惠
     */
    @PreAuthorize("@ss.hasPermi('system:discount:remove')")
    @Log(title = "档口客户优惠", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeCusProdDiscIds}")
    public R remove(@PathVariable Long[] storeCusProdDiscIds) {
        return success(storeCusProdDiscountSvc.deleteStoreCustomerProductDiscountByStoreCusProdDiscIds(storeCusProdDiscIds));
    }
}
