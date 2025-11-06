package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.DatabaseProduct;
import com.ruoyi.system.service.IDatabaseProductService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数据库产品Controller
 * 
 * @author ruoyi
 * @date 2024-01-20
 */
@RestController
@RequestMapping("/database/product")
public class DatabaseProductController extends BaseController
{
    @Autowired
    private IDatabaseProductService databaseProductService;

    /**
     * 查询数据库产品列表
     */
    @PreAuthorize("@ss.hasPermi('database:zhengyutian:list')")
    @GetMapping("/list")
    public TableDataInfo list(DatabaseProduct databaseProduct)
    {
        startPage();
        List<DatabaseProduct> list = databaseProductService.selectDatabaseProductList(databaseProduct);
        return getDataTable(list);
    }

    /**
     * 导出数据库产品列表
     */
    @PreAuthorize("@ss.hasPermi('database:zhengyutian:export')")
    @Log(title = "数据库产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DatabaseProduct databaseProduct)
    {
        List<DatabaseProduct> list = databaseProductService.selectDatabaseProductList(databaseProduct);
        ExcelUtil<DatabaseProduct> util = new ExcelUtil<DatabaseProduct>(DatabaseProduct.class);
        util.exportExcel(response, list, "数据库产品数据");
    }

    /**
     * 获取数据库产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('database:zhengyutian:query')")
    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable("productId") Long productId)
    {
        return AjaxResult.success(databaseProductService.selectDatabaseProductByProductId(productId));
    }

    /**
     * 新增数据库产品
     */
    @PreAuthorize("@ss.hasPermi('database:zhengyutian:add')")
    @Log(title = "数据库产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DatabaseProduct databaseProduct)
    {
        return toAjax(databaseProductService.insertDatabaseProduct(databaseProduct));
    }

    /**
     * 修改数据库产品
     */
    @PreAuthorize("@ss.hasPermi('database:zhengyutian:edit')")
    @Log(title = "数据库产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DatabaseProduct databaseProduct)
    {
        return toAjax(databaseProductService.updateDatabaseProduct(databaseProduct));
    }

    /**
     * 删除数据库产品
     */
    @PreAuthorize("@ss.hasPermi('database:zhengyutian:remove')")
    @Log(title = "数据库产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        return toAjax(databaseProductService.deleteDatabaseProductByProductIds(productIds));
    }
}