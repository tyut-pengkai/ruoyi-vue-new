package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.service.ISysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * fileController
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/files")
public class SysFileController extends XktBaseController {
    @Autowired
    private ISysFileService sysFileService;

    /**
     * 查询file列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysFile sysFile) {
        startPage();
        List<SysFile> list = sysFileService.selectSysFileList(sysFile);
        return getDataTable(list);
    }

    /**
     * 导出file列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:export')")
    @Log(title = "file", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysFile sysFile) {
        List<SysFile> list = sysFileService.selectSysFileList(sysFile);
        ExcelUtil<SysFile> util = new ExcelUtil<SysFile>(SysFile.class);
        util.exportExcel(response, list, "file数据");
    }

    /**
     * 获取file详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{fileId}")
    public R getInfo(@PathVariable("fileId") Long fileId) {
        return success(sysFileService.selectSysFileByFileId(fileId));
    }

    /**
     * 新增file
     */
    @PreAuthorize("@ss.hasPermi('system:file:add')")
    @Log(title = "file", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody SysFile sysFile) {
        return success(sysFileService.insertSysFile(sysFile));
    }

    /**
     * 修改file
     */
    @PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "file", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody SysFile sysFile) {
        return success(sysFileService.updateSysFile(sysFile));
    }

    /**
     * 删除file
     */
    @PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "file", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fileIds}")
    public R remove(@PathVariable Long[] fileIds) {
        return success(sysFileService.deleteSysFileByFileIds(fileIds));
    }
}
