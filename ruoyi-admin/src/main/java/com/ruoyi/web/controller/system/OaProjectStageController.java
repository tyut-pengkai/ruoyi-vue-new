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
import com.ruoyi.system.domain.OaProjectStage;
import com.ruoyi.system.service.IOaProjectStageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目阶段Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/stage")
public class OaProjectStageController extends BaseController
{
    @Autowired
    private IOaProjectStageService oaProjectStageService;

    /**
     * 查询项目阶段列表
     */
    @PreAuthorize("@ss.hasPermi('system:stage:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaProjectStage oaProjectStage)
    {
        startPage();
        List<OaProjectStage> list = oaProjectStageService.selectOaProjectStageList(oaProjectStage);
        return getDataTable(list);
    }

    /**
     * 导出项目阶段列表
     */
    @PreAuthorize("@ss.hasPermi('system:stage:export')")
    @Log(title = "项目阶段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OaProjectStage oaProjectStage)
    {
        List<OaProjectStage> list = oaProjectStageService.selectOaProjectStageList(oaProjectStage);
        ExcelUtil<OaProjectStage> util = new ExcelUtil<OaProjectStage>(OaProjectStage.class);
        util.exportExcel(response, list, "项目阶段数据");
    }

    /**
     * 获取项目阶段详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(oaProjectStageService.selectOaProjectStageById(id));
    }

    /**
     * 新增项目阶段
     */
    @PreAuthorize("@ss.hasPermi('system:stage:add')")
    @Log(title = "项目阶段", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaProjectStage oaProjectStage)
    {
        return toAjax(oaProjectStageService.insertOaProjectStage(oaProjectStage));
    }

    /**
     * 修改项目阶段
     */
    @PreAuthorize("@ss.hasPermi('system:stage:edit')")
    @Log(title = "项目阶段", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaProjectStage oaProjectStage)
    {
        return toAjax(oaProjectStageService.updateOaProjectStage(oaProjectStage));
    }

    /**
     * 删除项目阶段
     */
    @PreAuthorize("@ss.hasPermi('system:stage:remove')")
    @Log(title = "项目阶段", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaProjectStageService.deleteOaProjectStageByIds(ids));
    }
}
