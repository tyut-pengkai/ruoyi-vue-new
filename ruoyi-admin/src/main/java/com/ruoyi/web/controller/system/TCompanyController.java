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
import com.ruoyi.system.domain.TCompany;
import com.ruoyi.system.service.ITCompanyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 公司组织信息Controller
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@RestController
@RequestMapping("/system/company")
@Api(value = "公司组织信息控制器", tags = {"公司组织信息管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TCompanyController extends BaseController
{
    private final ITCompanyService tCompanyService;

    /**
     * 查询公司组织信息列表
     */
    @ApiOperation("查询公司组织信息列表")
    @PreAuthorize("@ss.hasPermi('system:company:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCompany tCompany) {
        startPage();
        List<TCompany> list = tCompanyService.list(new QueryWrapper<TCompany>(tCompany));
        return getDataTable(list);
    }

    /**
     * 导出公司组织信息列表
     */
    @ApiOperation("导出公司组织信息列表")
    @PreAuthorize("@ss.hasPermi('system:company:export')")
    @Log(title = "公司组织信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TCompany tCompany) {
        List<TCompany> list = tCompanyService.list(new QueryWrapper<TCompany>(tCompany));
        ExcelUtil<TCompany> util = new ExcelUtil<TCompany>(TCompany.class);
        return util.exportExcel(list, "公司组织信息数据");
    }

    /**
     * 获取公司组织信息详细信息
     */
    @ApiOperation("获取公司组织信息详细信息")
    @PreAuthorize("@ss.hasPermi('system:company:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tCompanyService.getById(id));
    }

    /**
     * 新增公司组织信息
     */
    @ApiOperation("新增公司组织信息")
    @PreAuthorize("@ss.hasPermi('system:company:add')")
    @Log(title = "公司组织信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCompany tCompany) {
        return toAjax(tCompanyService.save(tCompany));
    }

    /**
     * 修改公司组织信息
     */
    @ApiOperation("修改公司组织信息")
    @PreAuthorize("@ss.hasPermi('system:company:edit')")
    @Log(title = "公司组织信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCompany tCompany) {
        return toAjax(tCompanyService.updateById(tCompany));
    }

    /**
     * 删除公司组织信息
     */
    @ApiOperation("删除公司组织信息")
    @PreAuthorize("@ss.hasPermi('system:company:remove')")
    @Log(title = "公司组织信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tCompanyService.removeByIds(Arrays.asList(ids)));
    }
}
