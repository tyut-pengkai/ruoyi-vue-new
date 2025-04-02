package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeCustomer.StoreCusGeneralSaleVO;
import com.ruoyi.web.controller.xkt.vo.storeSale.StoreSalePageVO;
import com.ruoyi.web.controller.xkt.vo.storeSale.StoreSalePayStatusVO;
import com.ruoyi.web.controller.xkt.vo.storeSale.StoreSaleVO;
import com.ruoyi.xkt.domain.StoreSale;
import com.ruoyi.xkt.dto.storeSale.StoreSaleDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePageDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSalePayStatusDTO;
import com.ruoyi.xkt.service.IStoreSaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    /**
     * 根据当前客户查询最近的销售业绩，以及欠款金额
     */
    @PreAuthorize("@ss.hasPermi('system:sale:list')")
    @ApiOperation(value = "根据当前客户查询最近的销售业绩，以及欠款金额", httpMethod = "GET", response = R.class)
    @GetMapping("/cus-overall")
    public R getCusGeneralSale(@RequestParam("days") Integer days, @RequestParam("storeId") Long storeId,
                               @RequestParam("storeCusId") Long storeCusId) {
        return R.ok(BeanUtil.toBean(storeSaleService.getCusGeneralSale(days, storeId, storeCusId), StoreCusGeneralSaleVO.class));
    }

    /**
     * 查询档口销售出库列表
     */
    @PreAuthorize("@ss.hasPermi('system:sale:list')")
    @ApiOperation(value = "查询档口销售出库列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page> page(@Validated @RequestBody StoreSalePageVO salePageVO) {
        return R.ok(storeSaleService.page(BeanUtil.toBean(salePageVO, StoreSalePageDTO.class)));
    }

    /**
     * 新增档口销售出库
     */
    @PreAuthorize("@ss.hasPermi('system:sale:add')")
    @ApiOperation(value = "新增档口销售出库", httpMethod = "POST", response = R.class)
    @Log(title = "档口销售出库", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@Validated @RequestBody StoreSaleVO storeSaleVO) {
        return R.ok(storeSaleService.insertStoreSale(BeanUtil.toBean(storeSaleVO, StoreSaleDTO.class)));
    }

    /**
     * 返单后，更新档口销售出库
     */
    @PreAuthorize("@ss.hasPermi('system:sale:edit')")
    @Log(title = "修改档口销售出库", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "返单后，更新档口销售出库", httpMethod = "PUT", response = R.class)
    @PutMapping
    public R edit(@Validated @RequestBody StoreSaleVO storeSaleVO) {

        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存


        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        return R.ok(storeSaleService.updateStoreSale(BeanUtil.toBean(storeSaleVO, StoreSaleDTO.class)));
    }

    /**
     * 查询档口销售出库详情
     */
    @PreAuthorize("@ss.hasPermi('system:sale:query')")
    @ApiOperation(value = "查询档口销售出库详情", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeSaleId}")
    public R getInfo(@PathVariable("storeSaleId") Long storeSaleId) {
        return R.ok(storeSaleService.selectStoreSaleByStoreSaleId(storeSaleId));
    }

    /**
     * 客户欠款结算
     */
    @PreAuthorize("@ss.hasPermi('system:sale:edit')")
    @Log(title = "客户欠款结算", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "客户欠款结算", httpMethod = "PUT", response = R.class)
    @PutMapping("/clear-debt")
    public R clearStoreCusDebt(@Validated @RequestBody StoreSalePayStatusVO payStatusVO) {
        storeSaleService.clearStoreCusDebt(BeanUtil.toBean(payStatusVO, StoreSalePayStatusDTO.class));
        return R.ok();
    }

    /**
     * 删除档口销售出库
     */
    @PreAuthorize("@ss.hasPermi('system:sale:remove')")
    @Log(title = "档口销售出库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeSaleId}")
    public R remove(@PathVariable Long storeSaleId) {

        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存
        // TODO 扣件库存


        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        // TODO 增加库存
        return R.ok(storeSaleService.deleteStoreSaleByStoreSaleId(storeSaleId));
    }


    /**
     * 导出档口销售出库列表
     */
    @PreAuthorize("@ss.hasPermi('system:sale:export')")
    @Log(title = "档口销售出库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreSale storeSale) {
        List<StoreSale> list = storeSaleService.selectStoreSaleList(storeSale);
        ExcelUtil<StoreSale> util = new ExcelUtil<StoreSale>(StoreSale.class);
        util.exportExcel(response, list, "档口销售出库数据");
    }





}
