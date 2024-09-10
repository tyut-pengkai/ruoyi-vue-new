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
import com.ruoyi.system.domain.TClockinInfo;
import com.ruoyi.system.service.ITClockinInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 打卡信息Controller
 * 
 * @author ruoyi
 * @date 2024-09-10
 */
@RestController
@RequestMapping("/system/clockinfo")
@Api(value = "打卡信息控制器", tags = {"打卡信息管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TClockinInfoController extends BaseController
{
    private final ITClockinInfoService tClockinInfoService;

    /**
     * 查询打卡信息列表
     */
    @ApiOperation("查询打卡信息列表")
    @PreAuthorize("@ss.hasPermi('system:clockinfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(TClockinInfo tClockinInfo) {
        startPage();
        List<TClockinInfo> list = tClockinInfoService.list(new QueryWrapper<TClockinInfo>(tClockinInfo));
        return getDataTable(list);
    }

    /**
     * 导出打卡信息列表
     */
    @ApiOperation("导出打卡信息列表")
    @PreAuthorize("@ss.hasPermi('system:clockinfo:export')")
    @Log(title = "打卡信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TClockinInfo tClockinInfo) {
        List<TClockinInfo> list = tClockinInfoService.list(new QueryWrapper<TClockinInfo>(tClockinInfo));
        ExcelUtil<TClockinInfo> util = new ExcelUtil<TClockinInfo>(TClockinInfo.class);
        return util.exportExcel(list, "打卡信息数据");
    }

    /**
     * 获取打卡信息详细信息
     */
    @ApiOperation("获取打卡信息详细信息")
    @PreAuthorize("@ss.hasPermi('system:clockinfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tClockinInfoService.getById(id));
    }

    /**
     * 新增打卡信息
     */
    @ApiOperation("新增打卡信息")
    @PreAuthorize("@ss.hasPermi('system:clockinfo:add')")
    @Log(title = "打卡信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TClockinInfo tClockinInfo) {
        return toAjax(tClockinInfoService.save(tClockinInfo));
    }

    /**
     * 修改打卡信息
     */
    @ApiOperation("修改打卡信息")
    @PreAuthorize("@ss.hasPermi('system:clockinfo:edit')")
    @Log(title = "打卡信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TClockinInfo tClockinInfo) {
        return toAjax(tClockinInfoService.updateById(tClockinInfo));
    }

    /**
     * 删除打卡信息
     */
    @ApiOperation("删除打卡信息")
    @PreAuthorize("@ss.hasPermi('system:clockinfo:remove')")
    @Log(title = "打卡信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tClockinInfoService.removeByIds(Arrays.asList(ids)));
    }
}
