package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeCustomer.StoreCusPageVO;
import com.ruoyi.web.controller.xkt.vo.storeProdStock.StoreProdStockPageVO;
import com.ruoyi.web.controller.xkt.vo.storeProdStock.StoreProdStockVO;
import com.ruoyi.xkt.domain.StoreProductStock;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockPageDTO;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockUpdateDTO;
import com.ruoyi.xkt.service.IStoreProductStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 档口商品库存Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口商品库存")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/prod-stocks")
public class StoreProductStockController extends XktBaseController {

    final IStoreProductStockService storeProdStockService;


    // TODO 商品销售/出库 时读货号，然后查询当前货号颜色的库存
    // TODO 商品销售/出库 时读货号，然后查询当前货号颜色的库存

    // TODO 销售出库时扣件库存
    // TODO 销售出库时扣件库存
    // TODO 销售出库时扣件库存
    // TODO 销售出库时扣件库存
    // TODO 销售出库时扣件库存

    /**
     * 查询档口库存列表
     */
    @PreAuthorize("@ss.hasPermi('system:stock:list')")
    @ApiOperation(value = "查询档口库存列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page> selectPage(@Validated @RequestBody StoreProdStockPageVO pageVO) {
        return R.ok(storeProdStockService.selectPage(BeanUtil.toBean(pageVO, StoreProdStockPageDTO.class)));
    }


    /**
     * 档口商品库存清零
     */
    @PreAuthorize("@ss.hasPermi('system:stock:edit')")
    @ApiOperation(value = "档口商品库存清零", httpMethod = "PUT", response = R.class)
    @Log(title = "档口商品库存清零", businessType = BusinessType.UPDATE)
    @PutMapping("/clear-zero/{storeId}/{storeProdStockId}")
    public R<Integer> clearStockToZero(@PathVariable("storeId") Long storeId, @PathVariable("storeProdStockId") Long storeProdStockId) {
        return R.ok(storeProdStockService.clearStockToZero(storeId, storeProdStockId));
    }

    /**
     * 直接调整档口商品库存值
     */
    @PreAuthorize("@ss.hasPermi('system:stock:edit')")
    @ApiOperation(value = "直接调整档口商品库存值", httpMethod = "PUT", response = R.class)
    @Log(title = "直接调整档口商品库存值", businessType = BusinessType.UPDATE)
    @PutMapping("/update-stock/{storeId}")
    public R<Integer> updateStock(@PathVariable("storeId") Long storeId, @RequestBody StoreProdStockVO prodStockVO) {
        return R.ok(storeProdStockService.updateStock(storeId, Collections.singletonList(BeanUtil.toBean(prodStockVO, StoreProdStockUpdateDTO.class)), 0));
    }

    /**
     * 查询档口商品库存详情
     */
    @PreAuthorize("@ss.hasPermi('system:stock:query')")
    @ApiOperation(value = "查询档口商品库存详情", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}/{storeProdStockId}")
    public R getInfo(@PathVariable("storeId") Long storeId, @PathVariable("storeProdStockId") Long storeProdStockId) {
        return R.ok(storeProdStockService.selectByStoreProdStockId(storeId, storeProdStockId));
    }

    /**
     * 根据货号查询档口商品库存
     */
    @PreAuthorize("@ss.hasPermi('system:stock:query')")
    @ApiOperation(value = "根据货号查询档口商品库存", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}/prod-art-num/{prodArtNum}")
    public R getInfo(@PathVariable("storeId") Long storeId, @PathVariable("prodArtNum") String prodArtNum) {
        return R.ok(storeProdStockService.selectByStoreIdAndProdArtNum(storeId, prodArtNum));
    }














    /**
     * 查询档口商品库存列表
     */
    @PreAuthorize("@ss.hasPermi('system:stock:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductStock storeProductStock) {
        startPage();
        List<StoreProductStock> list = storeProdStockService.selectStoreProductStockList(storeProductStock);
        return getDataTable(list);
    }

    /**
     * 导出档口商品库存列表
     */
    @PreAuthorize("@ss.hasPermi('system:stock:export')")
    @Log(title = "档口商品库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductStock storeProductStock) {
        List<StoreProductStock> list = storeProdStockService.selectStoreProductStockList(storeProductStock);
        ExcelUtil<StoreProductStock> util = new ExcelUtil<StoreProductStock>(StoreProductStock.class);
        util.exportExcel(response, list, "档口商品库存数据");
    }


    /**
     * 新增档口商品库存
     */
    @PreAuthorize("@ss.hasPermi('system:stock:add')")
    @Log(title = "档口商品库存", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductStock storeProductStock) {
        return success(storeProdStockService.insertStoreProductStock(storeProductStock));
    }



    /**
     * 删除档口商品库存
     */
    @PreAuthorize("@ss.hasPermi('system:stock:remove')")
    @Log(title = "档口商品库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdStockIds}")
    public R remove(@PathVariable Long[] storeProdStockIds) {
        return success(storeProdStockService.deleteStoreProductStockByStoreProdStockIds(storeProdStockIds));
    }


}
