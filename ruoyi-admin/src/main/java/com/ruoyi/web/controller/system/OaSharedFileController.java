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
import com.ruoyi.system.domain.OaSharedFile;
import com.ruoyi.system.service.IOaSharedFileService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 共享文件Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/file")
public class OaSharedFileController extends BaseController
{
    @Autowired
    private IOaSharedFileService oaSharedFileService;

    /**
     * 查询共享文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaSharedFile oaSharedFile)
    {
        startPage();
        List<OaSharedFile> list = oaSharedFileService.selectOaSharedFileList(oaSharedFile);
        return getDataTable(list);
    }

    /**
     * 导出共享文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:export')")
    @Log(title = "共享文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OaSharedFile oaSharedFile)
    {
        List<OaSharedFile> list = oaSharedFileService.selectOaSharedFileList(oaSharedFile);
        ExcelUtil<OaSharedFile> util = new ExcelUtil<OaSharedFile>(OaSharedFile.class);
        util.exportExcel(response, list, "共享文件数据");
    }

    /**
     * 获取共享文件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(oaSharedFileService.selectOaSharedFileById(id));
    }

    /**
     * 新增共享文件
     */
    @PreAuthorize("@ss.hasPermi('system:file:add')")
    @Log(title = "共享文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaSharedFile oaSharedFile)
    {
        return toAjax(oaSharedFileService.insertOaSharedFile(oaSharedFile));
    }

    /**
     * 修改共享文件
     */
    @PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "共享文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaSharedFile oaSharedFile)
    {
        return toAjax(oaSharedFileService.updateOaSharedFile(oaSharedFile));
    }

    /**
     * 删除共享文件
     */
    @PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "共享文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaSharedFileService.deleteOaSharedFileByIds(ids));
    }
}
