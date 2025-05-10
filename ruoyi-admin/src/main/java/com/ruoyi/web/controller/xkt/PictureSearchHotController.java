package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.PictureSearchHot;
import com.ruoyi.xkt.service.IPictureSearchHotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 图搜热款Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/search-hots")
public class PictureSearchHotController extends XktBaseController {
    @Autowired
    private IPictureSearchHotService pictureSearchHotService;

    /**
     * 查询图搜热款列表
     */
    // @PreAuthorize("@ss.hasPermi('system:hot:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureSearchHot pictureSearchHot) {
        startPage();
        List<PictureSearchHot> list = pictureSearchHotService.selectPictureSearchHotList(pictureSearchHot);
        return getDataTable(list);
    }

    /**
     * 导出图搜热款列表
     */
    // @PreAuthorize("@ss.hasPermi('system:hot:export')")
    @Log(title = "图搜热款", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureSearchHot pictureSearchHot) {
        List<PictureSearchHot> list = pictureSearchHotService.selectPictureSearchHotList(pictureSearchHot);
        ExcelUtil<PictureSearchHot> util = new ExcelUtil<PictureSearchHot>(PictureSearchHot.class);
        util.exportExcel(response, list, "图搜热款数据");
    }

    /**
     * 获取图搜热款详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:hot:query')")
    @GetMapping(value = "/{picSearchHotId}")
    public R getInfo(@PathVariable("picSearchHotId") Long picSearchHotId) {
        return success(pictureSearchHotService.selectPictureSearchHotByPicSearchHotId(picSearchHotId));
    }

    /**
     * 新增图搜热款
     */
    // @PreAuthorize("@ss.hasPermi('system:hot:add')")
    @Log(title = "图搜热款", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody PictureSearchHot pictureSearchHot) {
        return success(pictureSearchHotService.insertPictureSearchHot(pictureSearchHot));
    }

    /**
     * 修改图搜热款
     */
    // @PreAuthorize("@ss.hasPermi('system:hot:edit')")
    @Log(title = "图搜热款", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody PictureSearchHot pictureSearchHot) {
        return success(pictureSearchHotService.updatePictureSearchHot(pictureSearchHot));
    }

    /**
     * 删除图搜热款
     */
    // @PreAuthorize("@ss.hasPermi('system:hot:remove')")
    @Log(title = "图搜热款", businessType = BusinessType.DELETE)
    @DeleteMapping("/{picSearchHotIds}")
    public R remove(@PathVariable Long[] picSearchHotIds) {
        return success(pictureSearchHotService.deletePictureSearchHotByPicSearchHotIds(picSearchHotIds));
    }
}
