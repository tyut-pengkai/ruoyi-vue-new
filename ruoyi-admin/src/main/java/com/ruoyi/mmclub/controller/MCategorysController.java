package com.ruoyi.mmclub.controller;

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
import com.ruoyi.mmclub.domain.MCategorys;
import com.ruoyi.mmclub.service.IMCategorysService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 医院分类管理Controller
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
@RestController
@RequestMapping("/mmclub/categorys")
public class MCategorysController extends BaseController
{
    @Autowired
    private IMCategorysService mCategorysService;

    /**
     * 查询医院分类管理列表
     */
    @PreAuthorize("@ss.hasPermi('mmclub:categorys:list')")
    @GetMapping("/list")
    public TableDataInfo list(MCategorys mCategorys)
    {
        startPage();
        List<MCategorys> list = mCategorysService.selectMCategorysList(mCategorys);
        return getDataTable(list);
    }

    /**
     * 导出医院分类管理列表
     */
    @PreAuthorize("@ss.hasPermi('mmclub:categorys:export')")
    @Log(title = "医院分类管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MCategorys mCategorys)
    {
        List<MCategorys> list = mCategorysService.selectMCategorysList(mCategorys);
        ExcelUtil<MCategorys> util = new ExcelUtil<MCategorys>(MCategorys.class);
        util.exportExcel(response, list, "医院分类管理数据");
    }

    /**
     * 获取医院分类管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mmclub:categorys:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mCategorysService.selectMCategorysById(id));
    }

    /**
     * 新增医院分类管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:categorys:add')")
    @Log(title = "医院分类管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MCategorys mCategorys)
    {
        return toAjax(mCategorysService.insertMCategorys(mCategorys));
    }

    /**
     * 修改医院分类管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:categorys:edit')")
    @Log(title = "医院分类管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MCategorys mCategorys)
    {
        return toAjax(mCategorysService.updateMCategorys(mCategorys));
    }

    /**
     * 删除医院分类管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:categorys:remove')")
    @Log(title = "医院分类管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mCategorysService.deleteMCategorysByIds(ids));
    }
}
