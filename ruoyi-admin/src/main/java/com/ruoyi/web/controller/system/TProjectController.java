package com.ruoyi.web.controller.system;

import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TProject;
import com.ruoyi.system.service.ITProjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目信息Controller
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@RestController
@RequestMapping("/system/project")
@Api(value = "项目信息控制器", tags = {"项目信息管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TProjectController extends BaseController
{
    private final ITProjectService tProjectService;

    /**
     * 查询项目信息列表
     */
    @ApiOperation("查询项目信息列表")
    @PreAuthorize("@ss.hasPermi('system:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(TProject tProject) {
        startPage();
        List<TProject> list = tProjectService.list(new QueryWrapper<TProject>(tProject));
        return getDataTable(list);
    }

    /**
     * 导出项目信息列表
     */
    @ApiOperation("导出项目信息列表")
    @PreAuthorize("@ss.hasPermi('system:project:export')")
    @Log(title = "项目信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TProject tProject) {
        List<TProject> list = tProjectService.list(new QueryWrapper<TProject>(tProject));
        ExcelUtil<TProject> util = new ExcelUtil<TProject>(TProject.class);
        return util.exportExcel(list, "项目信息数据");
    }

    /**
     * 获取项目信息详细信息
     */
    @ApiOperation("获取项目信息详细信息")
    @PreAuthorize("@ss.hasPermi('system:project:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tProjectService.getById(id));
    }

    /**
     * 新增项目信息
     */
    @ApiOperation("新增项目信息")
    @PreAuthorize("@ss.hasPermi('system:project:add')")
    @Log(title = "项目信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TProject tProject) {
        return toAjax(tProjectService.save(tProject));
    }

    /**
     * 修改项目信息
     */
    @ApiOperation("修改项目信息")
    @PreAuthorize("@ss.hasPermi('system:project:edit')")
    @Log(title = "项目信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TProject tProject) {
        return toAjax(tProjectService.updateById(tProject));
    }

    /**
     * 删除项目信息
     */
    @ApiOperation("删除项目信息")
    @PreAuthorize("@ss.hasPermi('system:project:remove')")
    @Log(title = "项目信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tProjectService.removeByIds(Arrays.asList(ids)));
    }
}
