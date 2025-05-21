package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeCustomer.StoreCusGeneralSaleVO;
import com.ruoyi.web.controller.xkt.vo.storeSale.*;
import com.ruoyi.xkt.dto.storeSale.*;
import com.ruoyi.xkt.service.IStoreSaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 档口销售出库Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口销售出库")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-sales")
public class StoreSaleController extends XktBaseController {

    final IStoreSaleService storeSaleService;

    @ApiOperation(value = "根据当前客户查询最近的销售业绩，以及欠款金额", httpMethod = "GET", response = R.class)
    @GetMapping("/cus-overall")
    public R<StoreCusGeneralSaleVO> getCusGeneralSale(@RequestParam("days") Integer days, @RequestParam("storeId") Long storeId,
                                                      @RequestParam("storeCusId") Long storeCusId) {
        return R.ok(BeanUtil.toBean(storeSaleService.getCusGeneralSale(days, storeId, storeCusId), StoreCusGeneralSaleVO.class));
    }

    @ApiOperation(value = "查询档口销售出库列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreSalePageResDTO>> page(@Validated @RequestBody StoreSalePageVO salePageVO) {
        return R.ok(storeSaleService.page(BeanUtil.toBean(salePageVO, StoreSalePageDTO.class)));
    }

    @ApiOperation(value = "新增档口销售出库", httpMethod = "POST", response = R.class)
    @Log(title = "档口销售出库", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody StoreSaleVO storeSaleVO) {
        return R.ok(storeSaleService.insertStoreSale(BeanUtil.toBean(storeSaleVO, StoreSaleDTO.class)));
    }

    @Log(title = "修改档口销售出库", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "返单后，更新档口销售出库", httpMethod = "PUT", response = R.class)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreSaleVO storeSaleVO) {
        return R.ok(storeSaleService.updateStoreSale(BeanUtil.toBean(storeSaleVO, StoreSaleDTO.class)));
    }

    @ApiOperation(value = "查询档口销售出库详情", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeSaleId}")
    public R<StoreSaleVO> getInfo(@PathVariable("storeSaleId") Long storeSaleId) {
        return R.ok(BeanUtil.toBean(storeSaleService.selectStoreSaleByStoreSaleId(storeSaleId), StoreSaleVO.class));
    }

    @ApiOperation(value = "查询档口今日销售额", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/today-sale/{storeId}")
    public R<StoreTodaySaleVO> getTodaySale(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeSaleService.getTodaySale(storeId), StoreTodaySaleVO.class));
    }

    @Log(title = "客户欠款结算", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "客户欠款结算", httpMethod = "PUT", response = R.class)
    @PutMapping("/clear-debt")
    public R<Integer> clearStoreCusDebt(@Validated @RequestBody StoreSalePayStatusVO payStatusVO) {
        return R.ok(storeSaleService.clearStoreCusDebt(BeanUtil.toBean(payStatusVO, StoreSalePayStatusDTO.class)));
    }

    @Log(title = "档口销售出库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeSaleId}")
    public R<Integer> remove(@PathVariable Long storeSaleId) {
        return R.ok(storeSaleService.deleteStoreSaleByStoreSaleId(storeSaleId));
    }

    @Log(title = "修改备注", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改备注", httpMethod = "PUT", response = R.class)
    @PutMapping("/memo")
    public R<Integer> updateMemo(@Validated @RequestBody StoreSaleUpdateMemoVO updateMemoVO) {
        return R.ok(storeSaleService.updateMemo(BeanUtil.toBean(updateMemoVO, StoreSaleUpdateMemoDTO.class)));
    }

}
