package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductFile;
import com.ruoyi.xkt.service.IStoreProductFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品文件Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-files")
public class StoreProductFileController extends XktBaseController {
    @Autowired
    private IStoreProductFileService storeProductFileService;

    /**
     * 查询档口商品文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductFile storeProductFile) {
        startPage();
        List<StoreProductFile> list = storeProductFileService.selectStoreProductFileList(storeProductFile);
        return getDataTable(list);
    }

    /**
     * 导出档口商品文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:file:export')")
    @Log(title = "档口商品文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductFile storeProductFile) {
        List<StoreProductFile> list = storeProductFileService.selectStoreProductFileList(storeProductFile);
        ExcelUtil<StoreProductFile> util = new ExcelUtil<StoreProductFile>(StoreProductFile.class);
        util.exportExcel(response, list, "档口商品文件数据");
    }

    /**
     * 获取档口商品文件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{storeProdFileId}")
    public R getInfo(@PathVariable("storeProdFileId") Long storeProdFileId) {
        return success(storeProductFileService.selectStoreProductFileByStoreProdFileId(storeProdFileId));
    }

    /**
     * 新增档口商品文件
     */
    @PreAuthorize("@ss.hasPermi('system:file:add')")
    @Log(title = "档口商品文件", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductFile storeProductFile) {
        return success(storeProductFileService.insertStoreProductFile(storeProductFile));
    }

    /**
     * 修改档口商品文件
     */
    @PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "档口商品文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductFile storeProductFile) {
        return success(storeProductFileService.updateStoreProductFile(storeProductFile));
    }

    /**
     * 删除档口商品文件
     */
    @PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "档口商品文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdFileIds}")
    public R remove(@PathVariable Long[] storeProdFileIds) {
        return success(storeProductFileService.deleteStoreProductFileByStoreProdFileIds(storeProdFileIds));
    }
}
