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
import com.ruoyi.system.domain.BjWorkType;
import com.ruoyi.system.service.IBjWorkTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 工种管理Controller
 * 
 * @author ssq
 * @date 2024-10-05
 */
@RestController
@RequestMapping("/system/worktype")
public class BjWorkTypeController extends BaseController
{
    @Autowired
    private IBjWorkTypeService bjWorkTypeService;

    /**
     * 查询工种管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:worktype:list')")
    @GetMapping("/list")
    public TableDataInfo list(BjWorkType bjWorkType)
    {
        startPage();
        List<BjWorkType> list = bjWorkTypeService.selectBjWorkTypeList(bjWorkType);
        return getDataTable(list);
    }

    /**
     * 导出工种管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:worktype:export')")
    @Log(title = "工种管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BjWorkType bjWorkType)
    {
        List<BjWorkType> list = bjWorkTypeService.selectBjWorkTypeList(bjWorkType);
        ExcelUtil<BjWorkType> util = new ExcelUtil<BjWorkType>(BjWorkType.class);
        util.exportExcel(response, list, "工种管理数据");
    }

    /**
     * 获取工种管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:worktype:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bjWorkTypeService.selectBjWorkTypeById(id));
    }

    /**
     * 新增工种管理
     */
    @PreAuthorize("@ss.hasPermi('system:worktype:add')")
    @Log(title = "工种管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BjWorkType bjWorkType)
    {
        return toAjax(bjWorkTypeService.insertBjWorkType(bjWorkType));
    }

    /**
     * 修改工种管理
     */
    @PreAuthorize("@ss.hasPermi('system:worktype:edit')")
    @Log(title = "工种管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BjWorkType bjWorkType)
    {
        return toAjax(bjWorkTypeService.updateBjWorkType(bjWorkType));
    }

    /**
     * 删除工种管理
     */
    @PreAuthorize("@ss.hasPermi('system:worktype:remove')")
    @Log(title = "工种管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bjWorkTypeService.deleteBjWorkTypeByIds(ids));
    }
}
