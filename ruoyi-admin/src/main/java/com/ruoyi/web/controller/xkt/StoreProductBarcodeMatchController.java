package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProdBarcodeMatch.BarcodeMatchVO;
import com.ruoyi.xkt.domain.StoreProductBarcodeMatch;
import com.ruoyi.xkt.dto.storeProdBarcodeMatch.BarcodeMatchDTO;
import com.ruoyi.xkt.service.IStoreProductBarcodeMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口条形码和第三方系统条形码匹配结果Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/barcode-matches")
public class StoreProductBarcodeMatchController extends XktBaseController {

    final IStoreProductBarcodeMatchService barcodeMatchService;

    /**
     * 修改档口条形码和第三方系统条形码匹配结果
     */
    // @PreAuthorize("@ss.hasPermi('system:match:edit')")
    @Log(title = "档口条形码和第三方系统条形码匹配结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public R updateBarcodeMatch(@Validated @RequestBody BarcodeMatchVO barcodeMatchVO) {
        return success(barcodeMatchService.updateBarcodeMatch(BeanUtil.toBean(barcodeMatchVO, BarcodeMatchDTO.class)));
    }


    /**
     * 查询档口条形码和第三方系统条形码匹配结果列表
     */
    // @PreAuthorize("@ss.hasPermi('system:match:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductBarcodeMatch storeProductBarcodeMatch) {
        startPage();
        List<StoreProductBarcodeMatch> list = barcodeMatchService.selectStoreProductBarcodeMatchList(storeProductBarcodeMatch);
        return getDataTable(list);
    }

    /**
     * 导出档口条形码和第三方系统条形码匹配结果列表
     */
    // @PreAuthorize("@ss.hasPermi('system:match:export')")
    @Log(title = "档口条形码和第三方系统条形码匹配结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductBarcodeMatch storeProductBarcodeMatch) {
        List<StoreProductBarcodeMatch> list = barcodeMatchService.selectStoreProductBarcodeMatchList(storeProductBarcodeMatch);
        ExcelUtil<StoreProductBarcodeMatch> util = new ExcelUtil<StoreProductBarcodeMatch>(StoreProductBarcodeMatch.class);
        util.exportExcel(response, list, "档口条形码和第三方系统条形码匹配结果数据");
    }

    /**
     * 获取档口条形码和第三方系统条形码匹配结果详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:match:query')")
    @GetMapping(value = "/{storeProdBarcodeMatchId}")
    public R getInfo(@PathVariable("storeProdBarcodeMatchId") Long storeProdBarcodeMatchId) {
        return success(barcodeMatchService.selectStoreProductBarcodeMatchByStoreProdBarcodeMatchId(storeProdBarcodeMatchId));
    }

    /**
     * 新增档口条形码和第三方系统条形码匹配结果
     */
    // @PreAuthorize("@ss.hasPermi('system:match:add')")
    @Log(title = "档口条形码和第三方系统条形码匹配结果", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductBarcodeMatch storeProductBarcodeMatch) {
        return success(barcodeMatchService.insertStoreProductBarcodeMatch(storeProductBarcodeMatch));
    }

    /**
     * 删除档口条形码和第三方系统条形码匹配结果
     */
    // @PreAuthorize("@ss.hasPermi('system:match:remove')")
    @Log(title = "档口条形码和第三方系统条形码匹配结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdBarcodeMatchIds}")
    public R remove(@PathVariable Long[] storeProdBarcodeMatchIds) {
        return success(barcodeMatchService.deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchIds(storeProdBarcodeMatchIds));
    }
}
