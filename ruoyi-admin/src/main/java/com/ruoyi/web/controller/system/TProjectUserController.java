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
import com.ruoyi.system.domain.TProjectUser;
import com.ruoyi.system.service.ITProjectUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目人员关联Controller
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@RestController
@RequestMapping("/system/projectUser")
@Api(value = "项目人员关联控制器", tags = {"项目人员关联管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TProjectUserController extends BaseController
{
    private final ITProjectUserService tProjectUserService;

    /**
     * 查询项目人员关联列表
     */
    @ApiOperation("查询项目人员关联列表")
    @PreAuthorize("@ss.hasPermi('system:projectUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(TProjectUser tProjectUser) {
        startPage();
        List<TProjectUser> list = tProjectUserService.list(new QueryWrapper<TProjectUser>(tProjectUser));
        return getDataTable(list);
    }

    /**
     * 导出项目人员关联列表
     */
    @ApiOperation("导出项目人员关联列表")
    @PreAuthorize("@ss.hasPermi('system:projectUser:export')")
    @Log(title = "项目人员关联", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TProjectUser tProjectUser) {
        List<TProjectUser> list = tProjectUserService.list(new QueryWrapper<TProjectUser>(tProjectUser));
        ExcelUtil<TProjectUser> util = new ExcelUtil<TProjectUser>(TProjectUser.class);
        return util.exportExcel(list, "项目人员关联数据");
    }

    /**
     * 获取项目人员关联详细信息
     */
    @ApiOperation("获取项目人员关联详细信息")
    @PreAuthorize("@ss.hasPermi('system:projectUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tProjectUserService.getById(id));
    }

    /**
     * 新增项目人员关联
     */
    @ApiOperation("新增项目人员关联")
    @PreAuthorize("@ss.hasPermi('system:projectUser:add')")
    @Log(title = "项目人员关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TProjectUser tProjectUser) {
        return toAjax(tProjectUserService.save(tProjectUser));
    }

    /**
     * 修改项目人员关联
     */
    @ApiOperation("修改项目人员关联")
    @PreAuthorize("@ss.hasPermi('system:projectUser:edit')")
    @Log(title = "项目人员关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TProjectUser tProjectUser) {
        return toAjax(tProjectUserService.updateById(tProjectUser));
    }

    /**
     * 删除项目人员关联
     */
    @ApiOperation("删除项目人员关联")
    @PreAuthorize("@ss.hasPermi('system:projectUser:remove')")
    @Log(title = "项目人员关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tProjectUserService.removeByIds(Arrays.asList(ids)));
    }
}
