package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.PictureSearch;
import com.ruoyi.xkt.service.IPictureSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 以图搜款Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/pic-searches")
public class PictureSearchController extends XktBaseController {
    @Autowired
    private IPictureSearchService pictureSearchService;

    /**
     * 查询以图搜款列表
     */
    // @PreAuthorize("@ss.hasPermi('system:search:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureSearch pictureSearch) {
        startPage();
        List<PictureSearch> list = pictureSearchService.selectPictureSearchList(pictureSearch);
        return getDataTable(list);
    }

    /**
     * 导出以图搜款列表
     */
    // @PreAuthorize("@ss.hasPermi('system:search:export')")
    @Log(title = "以图搜款", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureSearch pictureSearch) {
        List<PictureSearch> list = pictureSearchService.selectPictureSearchList(pictureSearch);
        ExcelUtil<PictureSearch> util = new ExcelUtil<PictureSearch>(PictureSearch.class);
        util.exportExcel(response, list, "以图搜款数据");
    }

    /**
     * 获取以图搜款详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:search:query')")
    @GetMapping(value = "/{picSearchId}")
    public R getInfo(@PathVariable("picSearchId") Long picSearchId) {
        return success(pictureSearchService.selectPictureSearchByPicSearchId(picSearchId));
    }

    /**
     * 新增以图搜款
     */
    // @PreAuthorize("@ss.hasPermi('system:search:add')")
    @Log(title = "以图搜款", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody PictureSearch pictureSearch) {
        return success(pictureSearchService.insertPictureSearch(pictureSearch));
    }

    /**
     * 修改以图搜款
     */
    // @PreAuthorize("@ss.hasPermi('system:search:edit')")
    @Log(title = "以图搜款", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody PictureSearch pictureSearch) {
        return success(pictureSearchService.updatePictureSearch(pictureSearch));
    }

    /**
     * 删除以图搜款
     */
    // @PreAuthorize("@ss.hasPermi('system:search:remove')")
    @Log(title = "以图搜款", businessType = BusinessType.DELETE)
    @DeleteMapping("/{picSearchIds}")
    public R remove(@PathVariable Long[] picSearchIds) {
        return success(pictureSearchService.deletePictureSearchByPicSearchIds(picSearchIds));
    }
}
