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
import com.ruoyi.system.domain.TMaterial;
import com.ruoyi.system.service.ITMaterialService;
import com.ruoyi.web.util.MaterialTreeUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物料Controller
 * 
 * @author ruoyi
 * @date 2024-10-31
 */
@RestController
@RequestMapping("/system/material")
public class TMaterialController extends BaseController
{
    @Autowired
    private ITMaterialService tMaterialService;

    /**
     * 查询物料列表
     */
    @PreAuthorize("@ss.hasPermi('system:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(TMaterial tMaterial)
    {
        startPage();
        List<TMaterial> list = tMaterialService.selectTMaterialList(tMaterial);
        return getDataTable(list);
    }

    /**
     * 导出物料列表
     */
    @PreAuthorize("@ss.hasPermi('system:material:export')")
    @Log(title = "物料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMaterial tMaterial)
    {
        List<TMaterial> list = tMaterialService.selectTMaterialList(tMaterial);
        ExcelUtil<TMaterial> util = new ExcelUtil<TMaterial>(TMaterial.class);
        util.exportExcel(response, list, "物料数据");
    }
    
    @PreAuthorize("@ss.hasPermi('system:material:query')")
    @GetMapping(value = "/tree")
    public AjaxResult tree() {
    	List<TMaterial> list = tMaterialService.selectTMaterialList(null);
    	if(list != null && list.size() > 0) {
    		MaterialTreeUtil tree = new MaterialTreeUtil(list);
    		return success(tree.buildMaterTree());
    	}
        return success();
    }

    /**
     * 获取物料详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:material:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tMaterialService.selectTMaterialById(id));
    }

    /**
     * 新增物料
     */
    @PreAuthorize("@ss.hasPermi('system:material:add')")
    @Log(title = "物料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMaterial tMaterial)
    {
        return toAjax(tMaterialService.insertTMaterial(tMaterial));
    }

    /**
     * 修改物料
     */
    @PreAuthorize("@ss.hasPermi('system:material:edit')")
    @Log(title = "物料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMaterial tMaterial)
    {
        return toAjax(tMaterialService.updateTMaterial(tMaterial));
    }

    /**
     * 删除物料
     */
    @PreAuthorize("@ss.hasPermi('system:material:remove')")
    @Log(title = "物料", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMaterialService.deleteTMaterialByIds(ids));
    }
}
