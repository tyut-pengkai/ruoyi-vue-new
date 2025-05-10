package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.PictureSearchResult;
import com.ruoyi.xkt.service.IPictureSearchResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 以图搜款结果Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/search-results")
public class PictureSearchResultController extends XktBaseController {
    @Autowired
    private IPictureSearchResultService pictureSearchResultService;

    /**
     * 查询以图搜款结果列表
     */
    // @PreAuthorize("@ss.hasPermi('system:result:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureSearchResult pictureSearchResult) {
        startPage();
        List<PictureSearchResult> list = pictureSearchResultService.selectPictureSearchResultList(pictureSearchResult);
        return getDataTable(list);
    }

    /**
     * 导出以图搜款结果列表
     */
    // @PreAuthorize("@ss.hasPermi('system:result:export')")
    @Log(title = "以图搜款结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureSearchResult pictureSearchResult) {
        List<PictureSearchResult> list = pictureSearchResultService.selectPictureSearchResultList(pictureSearchResult);
        ExcelUtil<PictureSearchResult> util = new ExcelUtil<PictureSearchResult>(PictureSearchResult.class);
        util.exportExcel(response, list, "以图搜款结果数据");
    }

    /**
     * 获取以图搜款结果详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:result:query')")
    @GetMapping(value = "/{picSearchResId}")
    public R getInfo(@PathVariable("picSearchResId") Long picSearchResId) {
        return success(pictureSearchResultService.selectPictureSearchResultByPicSearchResId(picSearchResId));
    }

    /**
     * 新增以图搜款结果
     */
    // @PreAuthorize("@ss.hasPermi('system:result:add')")
    @Log(title = "以图搜款结果", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody PictureSearchResult pictureSearchResult) {
        return success(pictureSearchResultService.insertPictureSearchResult(pictureSearchResult));
    }

    /**
     * 修改以图搜款结果
     */
    // @PreAuthorize("@ss.hasPermi('system:result:edit')")
    @Log(title = "以图搜款结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody PictureSearchResult pictureSearchResult) {
        return success(pictureSearchResultService.updatePictureSearchResult(pictureSearchResult));
    }

    /**
     * 删除以图搜款结果
     */
    // @PreAuthorize("@ss.hasPermi('system:result:remove')")
    @Log(title = "以图搜款结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{picSearchResIds}")
    public R remove(@PathVariable Long[] picSearchResIds) {
        return success(pictureSearchResultService.deletePictureSearchResultByPicSearchResIds(picSearchResIds));
    }
}
