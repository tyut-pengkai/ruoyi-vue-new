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
import com.ruoyi.system.domain.OaProject;
import com.ruoyi.system.service.IOaProjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目管理主Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/project")
public class OaProjectController extends BaseController
{
    @Autowired
    private IOaProjectService oaProjectService;

    /**
     * 查询项目管理主列表
     */
    @PreAuthorize("@ss.hasPermi('system:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaProject oaProject)
    {
        startPage();
        List<OaProject> list = oaProjectService.selectOaProjectList(oaProject);
        return getDataTable(list);
    }

    /**
     * 导出项目管理主列表
     */
    @PreAuthorize("@ss.hasPermi('system:project:export')")
    @Log(title = "项目管理主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OaProject oaProject)
    {
        List<OaProject> list = oaProjectService.selectOaProjectList(oaProject);
        ExcelUtil<OaProject> util = new ExcelUtil<OaProject>(OaProject.class);
        util.exportExcel(response, list, "项目管理主数据");
    }

    /**
     * 获取项目管理主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:project:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(oaProjectService.selectOaProjectById(id));
    }

    /**
     * 新增项目管理主
     */
    @PreAuthorize("@ss.hasPermi('system:project:add')")
    @Log(title = "项目管理主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaProject oaProject)
    {
        return toAjax(oaProjectService.insertOaProject(oaProject));
    }

    /**
     * 修改项目管理主
     */
    @PreAuthorize("@ss.hasPermi('system:project:edit')")
    @Log(title = "项目管理主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaProject oaProject)
    {
        return toAjax(oaProjectService.updateOaProject(oaProject));
    }

    /**
     * 删除项目管理主
     */
    @PreAuthorize("@ss.hasPermi('system:project:remove')")
    @Log(title = "项目管理主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaProjectService.deleteOaProjectByIds(ids));
    }
}
