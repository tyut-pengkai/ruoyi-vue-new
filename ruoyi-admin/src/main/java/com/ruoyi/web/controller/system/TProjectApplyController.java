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
import com.ruoyi.system.domain.TProjectApply;
import com.ruoyi.system.service.ITProjectApplyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目申请信息Controller
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@RestController
@RequestMapping("/system/apply")
@Api(value = "项目申请信息控制器", tags = {"项目申请信息管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TProjectApplyController extends BaseController
{
    private final ITProjectApplyService tProjectApplyService;

    /**
     * 查询项目申请信息列表
     */
    @ApiOperation("查询项目申请信息列表")
    @PreAuthorize("@ss.hasPermi('system:apply:list')")
    @GetMapping("/list")
    public TableDataInfo list(TProjectApply tProjectApply) {
        startPage();
        List<TProjectApply> list = tProjectApplyService.list(new QueryWrapper<TProjectApply>(tProjectApply));
        return getDataTable(list);
    }

    /**
     * 导出项目申请信息列表
     */
    @ApiOperation("导出项目申请信息列表")
    @PreAuthorize("@ss.hasPermi('system:apply:export')")
    @Log(title = "项目申请信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TProjectApply tProjectApply) {
        List<TProjectApply> list = tProjectApplyService.list(new QueryWrapper<TProjectApply>(tProjectApply));
        ExcelUtil<TProjectApply> util = new ExcelUtil<TProjectApply>(TProjectApply.class);
        return util.exportExcel(list, "项目申请信息数据");
    }

    /**
     * 获取项目申请信息详细信息
     */
    @ApiOperation("获取项目申请信息详细信息")
    @PreAuthorize("@ss.hasPermi('system:apply:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tProjectApplyService.getById(id));
    }

    /**
     * 新增项目申请信息
     */
    @ApiOperation("新增项目申请信息")
    @PreAuthorize("@ss.hasPermi('system:apply:add')")
    @Log(title = "项目申请信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TProjectApply tProjectApply) {
        return toAjax(tProjectApplyService.save(tProjectApply));
    }

    /**
     * 修改项目申请信息
     */
    @ApiOperation("修改项目申请信息")
    @PreAuthorize("@ss.hasPermi('system:apply:edit')")
    @Log(title = "项目申请信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TProjectApply tProjectApply) {
        return toAjax(tProjectApplyService.updateById(tProjectApply));
    }

    /**
     * 删除项目申请信息
     */
    @ApiOperation("删除项目申请信息")
    @PreAuthorize("@ss.hasPermi('system:apply:remove')")
    @Log(title = "项目申请信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tProjectApplyService.removeByIds(Arrays.asList(ids)));
    }
}
