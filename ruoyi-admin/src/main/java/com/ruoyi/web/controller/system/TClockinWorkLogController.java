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
import com.ruoyi.system.domain.TClockinWorkLog;
import com.ruoyi.system.service.ITClockinWorkLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 日志信息Controller
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@RestController
@RequestMapping("/system/log")
@Api(value = "日志信息控制器", tags = {"日志信息管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TClockinWorkLogController extends BaseController
{
    private final ITClockinWorkLogService tClockinWorkLogService;

    /**
     * 查询日志信息列表
     */
    @ApiOperation("查询日志信息列表")
    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(TClockinWorkLog tClockinWorkLog) {
        startPage();
        List<TClockinWorkLog> list = tClockinWorkLogService.list(new QueryWrapper<TClockinWorkLog>(tClockinWorkLog));
        return getDataTable(list);
    }

    /**
     * 导出日志信息列表
     */
    @ApiOperation("导出日志信息列表")
    @PreAuthorize("@ss.hasPermi('system:log:export')")
    @Log(title = "日志信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TClockinWorkLog tClockinWorkLog) {
        List<TClockinWorkLog> list = tClockinWorkLogService.list(new QueryWrapper<TClockinWorkLog>(tClockinWorkLog));
        ExcelUtil<TClockinWorkLog> util = new ExcelUtil<TClockinWorkLog>(TClockinWorkLog.class);
        return util.exportExcel(list, "日志信息数据");
    }

    /**
     * 获取日志信息详细信息
     */
    @ApiOperation("获取日志信息详细信息")
    @PreAuthorize("@ss.hasPermi('system:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tClockinWorkLogService.getById(id));
    }

    /**
     * 新增日志信息
     */
    @ApiOperation("新增日志信息")
    @PreAuthorize("@ss.hasPermi('system:log:add')")
    @Log(title = "日志信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TClockinWorkLog tClockinWorkLog) {
        return toAjax(tClockinWorkLogService.save(tClockinWorkLog));
    }

    /**
     * 修改日志信息
     */
    @ApiOperation("修改日志信息")
    @PreAuthorize("@ss.hasPermi('system:log:edit')")
    @Log(title = "日志信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TClockinWorkLog tClockinWorkLog) {
        return toAjax(tClockinWorkLogService.updateById(tClockinWorkLog));
    }

    /**
     * 删除日志信息
     */
    @ApiOperation("删除日志信息")
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "日志信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tClockinWorkLogService.removeByIds(Arrays.asList(ids)));
    }
}
