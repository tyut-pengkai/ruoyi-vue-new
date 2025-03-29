package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storePordColor.StoreProdColorResVO;
import com.ruoyi.xkt.domain.StoreProductColor;
import com.ruoyi.xkt.service.IStoreProductColorService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口当前商品颜色Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口商品颜色")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/prod-colors")
public class StoreProductColorController extends XktBaseController {

    final IStoreProductColorService storeProdColorService;

    /**
     * 模糊查询档口所有的商品颜色分类
     */
    @PreAuthorize("@ss.hasPermi('system:color:query')")
    @GetMapping(value = "/fuzzy")
    public R fuzzyQueryColorList(@RequestParam(value = "prodArtNum", required = false) String prodArtNum,
                                 @RequestParam("storeId") Long storeId) {
        return success(BeanUtil.copyToList(storeProdColorService.fuzzyQueryColorList(storeId, prodArtNum), StoreProdColorResVO.class));
    }

    /**
     * 查询档口当前商品颜色列表
     */
    @PreAuthorize("@ss.hasPermi('system:color:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductColor storeProductColor) {
        startPage();
        List<StoreProductColor> list = storeProdColorService.selectStoreProductColorList(storeProductColor);
        return getDataTable(list);
    }

    /**
     * 导出档口当前商品颜色列表
     */
    @PreAuthorize("@ss.hasPermi('system:color:export')")
    @Log(title = "档口当前商品颜色", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductColor storeProductColor) {
        List<StoreProductColor> list = storeProdColorService.selectStoreProductColorList(storeProductColor);
        ExcelUtil<StoreProductColor> util = new ExcelUtil<StoreProductColor>(StoreProductColor.class);
        util.exportExcel(response, list, "档口当前商品颜色数据");
    }

    /**
     * 获取档口当前商品颜色详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:color:query')")
    @GetMapping(value = "/{storeProdColorId}")
    public R getInfo(@PathVariable("storeProdColorId") Long storeProdColorId) {
        return success(storeProdColorService.selectStoreProductColorByStoreProdColorId(storeProdColorId));
    }

    /**
     * 新增档口当前商品颜色
     */
    @PreAuthorize("@ss.hasPermi('system:color:add')")
    @Log(title = "档口当前商品颜色", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductColor storeProductColor) {
        return success(storeProdColorService.insertStoreProductColor(storeProductColor));
    }

    /**
     * 修改档口当前商品颜色
     */
    @PreAuthorize("@ss.hasPermi('system:color:edit')")
    @Log(title = "档口当前商品颜色", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductColor storeProductColor) {
        return success(storeProdColorService.updateStoreProductColor(storeProductColor));
    }

    /**
     * 删除档口当前商品颜色
     */
    @PreAuthorize("@ss.hasPermi('system:color:remove')")
    @Log(title = "档口当前商品颜色", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdColorIds}")
    public R remove(@PathVariable Long[] storeProdColorIds) {
        return success(storeProdColorService.deleteStoreProductColorByStoreProdColorIds(storeProdColorIds));
    }

}
