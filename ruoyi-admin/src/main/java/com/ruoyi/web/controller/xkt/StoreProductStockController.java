package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProdStock.*;
import com.ruoyi.web.controller.xkt.vo.storeProdStorage.StoreStorageExportVO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageDetailDownloadDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageExportDTO;
import com.ruoyi.xkt.dto.storeProductStock.*;
import com.ruoyi.xkt.service.IStoreProductStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    @ApiOperation(value = "查询档口库存列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreProdStockPageResDTO>> selectPage(@Validated @RequestBody StoreProdStockPageVO pageVO) {
        return R.ok(storeProdStockService.selectPage(BeanUtil.toBean(pageVO, StoreProdStockPageDTO.class)));
    }

    @ApiOperation(value = "档口商品库存清零", httpMethod = "PUT", response = R.class)
    @Log(title = "档口商品库存清零", businessType = BusinessType.UPDATE)
    @PutMapping("/clear-zero")
    public R<Integer> clearStockToZero(@Validated @RequestBody StoreProdStockClearZeroVO clearZeroVO) {
        return R.ok(storeProdStockService.clearStockToZero(BeanUtil.toBean(clearZeroVO, StoreProdStockClearZeroDTO.class)));
    }

    @ApiOperation(value = "直接调整档口商品库存值", httpMethod = "PUT", response = R.class)
    @Log(title = "直接调整档口商品库存值", businessType = BusinessType.UPDATE)
    @PutMapping("/update-stock/{storeId}")
    public R<Integer> updateStock(@PathVariable("storeId") Long storeId, @Validated @RequestBody StoreProdStockVO prodStockVO) {
        return R.ok(storeProdStockService.updateStock(storeId, Collections.singletonList(BeanUtil.toBean(prodStockVO, StoreProdStockDTO.class)), 0));
    }

    @ApiOperation(value = "库存盘点提交", httpMethod = "PUT", response = R.class)
    @Log(title = "库存盘点提交", businessType = BusinessType.UPDATE)
    @PutMapping("/check/update-stock/{storeId}")
    public R<Integer> checkAndUpdateStock(@PathVariable("storeId") Long storeId, @Validated @RequestBody StoreProdCheckStockVO checkStockVO) {
        return R.ok(storeProdStockService.checkAndUpdateStock(storeId, BeanUtil.toBean(checkStockVO, StoreProdCheckStockDTO.class)));
    }

    @ApiOperation(value = "查询档口商品库存详情", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}/{storeProdStockId}")
    public R<StoreProdStockResVO> getInfo(@PathVariable("storeId") Long storeId, @PathVariable("storeProdStockId") Long storeProdStockId) {
        return R.ok(BeanUtil.toBean(storeProdStockService.selectByStoreProdStockId(storeId, storeProdStockId), StoreProdStockResVO.class));
    }

    @ApiOperation(value = "根据货号查询档口商品库存", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}/prod-art-num/{prodArtNum}")
    public R<List<StoreProdStockResVO>> getByStoreIdAndProdArtNum(@PathVariable("storeId") Long storeId, @PathVariable("prodArtNum") String prodArtNum) {
        return R.ok(BeanUtil.copyToList(storeProdStockService.selectByStoreIdAndProdArtNum(storeId, prodArtNum), StoreProdStockResVO.class));
    }

    @ApiOperation(value = "销售出库，输入货号，查询客户优惠信息及当前货号颜色的库存", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/stock-cus-discount")
    public R<StoreProdStockAndDiscountResVO> getStockAndCusDiscount(@Validated @RequestBody StoreProdStockAndDiscountVO stockAndDiscountVO) {
        return R.ok(BeanUtil.toBean(storeProdStockService.getStockAndCusDiscount(BeanUtil
                .toBean(stockAndDiscountVO, StoreProdStockAndDiscountDTO.class)), StoreProdStockAndDiscountResVO.class));
    }

    @ApiOperation(value = "导出库存", httpMethod = "POST", response = R.class)
    @Log(title = "导出库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Validated @RequestBody StoreProdStockExportVO exportVO) throws UnsupportedEncodingException {
        List<StoreProdStockDownloadDTO> downloadList = storeProdStockService.export(BeanUtil.toBean(exportVO, StoreProdStockExportDTO.class));
        ExcelUtil<StoreProdStockDownloadDTO> util = new ExcelUtil<>(StoreProdStockDownloadDTO.class);
        // 设置下载excel名
        String encodedFileName = URLEncoder.encode("库存明细" + DateUtils.getDate(),  "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        util.exportExcel(response, downloadList, "库存明细");
    }



}
