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
import com.ruoyi.system.domain.FileInfo;
import com.ruoyi.system.service.IFileInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 文件Controller
 * 
 * @author ruoyi
 * @date 2025-06-01
 */
@RestController
@RequestMapping("/system/info")
public class FileInfoController extends BaseController
{
    @Autowired
    private IFileInfoService fileInfoService;

    /**
     * 查询文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(FileInfo fileInfo)
    {
        startPage();
        List<FileInfo> list = fileInfoService.selectFileInfoList(fileInfo);
        return getDataTable(list);
    }

    /**
     * 导出文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:export')")
    @Log(title = "文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FileInfo fileInfo)
    {
        List<FileInfo> list = fileInfoService.selectFileInfoList(fileInfo);
        ExcelUtil<FileInfo> util = new ExcelUtil<FileInfo>(FileInfo.class);
        util.exportExcel(response, list, "文件数据");
    }

    /**
     * 获取文件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fileInfoService.selectFileInfoById(id));
    }

    /**
     * 新增文件
     */
    @PreAuthorize("@ss.hasPermi('system:info:add')")
    @Log(title = "文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FileInfo fileInfo)
    {
        return toAjax(fileInfoService.insertFileInfo(fileInfo));
    }

    /**
     * 修改文件
     */
    @PreAuthorize("@ss.hasPermi('system:info:edit')")
    @Log(title = "文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FileInfo fileInfo)
    {
        return toAjax(fileInfoService.updateFileInfo(fileInfo));
    }

    /**
     * 删除文件
     */
    @PreAuthorize("@ss.hasPermi('system:info:remove')")
    @Log(title = "文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fileInfoService.deleteFileInfoByIds(ids));
    }
}
