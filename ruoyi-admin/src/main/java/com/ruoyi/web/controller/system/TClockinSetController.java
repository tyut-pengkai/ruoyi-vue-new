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
import com.ruoyi.system.domain.TClockinSet;
import com.ruoyi.system.service.ITClockinSetService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 打卡时间设置Controller
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@RestController
@RequestMapping("/system/set")
@Api(value = "打卡时间设置控制器", tags = {"打卡时间设置管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TClockinSetController extends BaseController
{
    private final ITClockinSetService tClockinSetService;

    /**
     * 查询打卡时间设置列表
     */
    @ApiOperation("查询打卡时间设置列表")
    @PreAuthorize("@ss.hasPermi('system:set:list')")
    @GetMapping("/list")
    public TableDataInfo list(TClockinSet tClockinSet) {
        startPage();
        List<TClockinSet> list = tClockinSetService.list(new QueryWrapper<TClockinSet>(tClockinSet));
        return getDataTable(list);
    }

    /**
     * 导出打卡时间设置列表
     */
    @ApiOperation("导出打卡时间设置列表")
    @PreAuthorize("@ss.hasPermi('system:set:export')")
    @Log(title = "打卡时间设置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TClockinSet tClockinSet) {
        List<TClockinSet> list = tClockinSetService.list(new QueryWrapper<TClockinSet>(tClockinSet));
        ExcelUtil<TClockinSet> util = new ExcelUtil<TClockinSet>(TClockinSet.class);
        return util.exportExcel(list, "打卡时间设置数据");
    }

    /**
     * 获取打卡时间设置详细信息
     */
    @ApiOperation("获取打卡时间设置详细信息")
    @PreAuthorize("@ss.hasPermi('system:set:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tClockinSetService.getById(id));
    }

    /**
     * 新增打卡时间设置
     */
    @ApiOperation("新增打卡时间设置")
    @PreAuthorize("@ss.hasPermi('system:set:add')")
    @Log(title = "打卡时间设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TClockinSet tClockinSet) {
        return toAjax(tClockinSetService.save(tClockinSet));
    }

    /**
     * 修改打卡时间设置
     */
    @ApiOperation("修改打卡时间设置")
    @PreAuthorize("@ss.hasPermi('system:set:edit')")
    @Log(title = "打卡时间设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TClockinSet tClockinSet) {
        return toAjax(tClockinSetService.updateById(tClockinSet));
    }

    /**
     * 删除打卡时间设置
     */
    @ApiOperation("删除打卡时间设置")
    @PreAuthorize("@ss.hasPermi('system:set:remove')")
    @Log(title = "打卡时间设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tClockinSetService.removeByIds(Arrays.asList(ids)));
    }
}
