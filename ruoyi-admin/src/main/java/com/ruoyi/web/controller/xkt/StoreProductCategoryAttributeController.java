package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductCategoryAttribute;
import com.ruoyi.xkt.service.IStoreProductCategoryAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品类目信息Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/cate-attrs")
public class StoreProductCategoryAttributeController extends XktBaseController {
    @Autowired
    private IStoreProductCategoryAttributeService storeProductCategoryAttributeService;

    /**
     * 查询档口商品类目信息列表
     */
    // @PreAuthorize("@ss.hasPermi('system:attribute:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductCategoryAttribute storeProductCategoryAttribute) {
        startPage();
        List<StoreProductCategoryAttribute> list = storeProductCategoryAttributeService.selectStoreProductCategoryAttributeList(storeProductCategoryAttribute);
        return getDataTable(list);
    }

    /**
     * 导出档口商品类目信息列表
     */
    // @PreAuthorize("@ss.hasPermi('system:attribute:export')")
    @Log(title = "档口商品类目信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductCategoryAttribute storeProductCategoryAttribute) {
        List<StoreProductCategoryAttribute> list = storeProductCategoryAttributeService.selectStoreProductCategoryAttributeList(storeProductCategoryAttribute);
        ExcelUtil<StoreProductCategoryAttribute> util = new ExcelUtil<StoreProductCategoryAttribute>(StoreProductCategoryAttribute.class);
        util.exportExcel(response, list, "档口商品类目信息数据");
    }

    /**
     * 获取档口商品类目信息详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:attribute:query')")
    @GetMapping(value = "/{storeProdAttrId}")
    public R getInfo(@PathVariable("storeProdAttrId") Long storeProdAttrId) {
        return success(storeProductCategoryAttributeService.selectStoreProductCategoryAttributeByStoreProdAttrId(storeProdAttrId));
    }

    /**
     * 新增档口商品类目信息
     */
    // @PreAuthorize("@ss.hasPermi('system:attribute:add')")
    @Log(title = "档口商品类目信息", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductCategoryAttribute storeProductCategoryAttribute) {
        return success(storeProductCategoryAttributeService.insertStoreProductCategoryAttribute(storeProductCategoryAttribute));
    }

    /**
     * 修改档口商品类目信息
     */
    // @PreAuthorize("@ss.hasPermi('system:attribute:edit')")
    @Log(title = "档口商品类目信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductCategoryAttribute storeProductCategoryAttribute) {
        return success(storeProductCategoryAttributeService.updateStoreProductCategoryAttribute(storeProductCategoryAttribute));
    }

    /**
     * 删除档口商品类目信息
     */
    // @PreAuthorize("@ss.hasPermi('system:attribute:remove')")
    @Log(title = "档口商品类目信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdAttrIds}")
    public R remove(@PathVariable Long[] storeProdAttrIds) {
        return success(storeProductCategoryAttributeService.deleteStoreProductCategoryAttributeByStoreProdAttrIds(storeProdAttrIds));
    }
}
