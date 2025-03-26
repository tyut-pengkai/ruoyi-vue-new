package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.UserFavorites;
import com.ruoyi.xkt.service.IUserFavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户收藏商品Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/user-faves")
public class UserFavoritesController extends BaseController {
    @Autowired
    private IUserFavoritesService userFavoritesService;

    /**
     * 查询用户收藏商品列表
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserFavorites userFavorites) {
        startPage();
        List<UserFavorites> list = userFavoritesService.selectUserFavoritesList(userFavorites);
        return getDataTable(list);
    }

    /**
     * 导出用户收藏商品列表
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:export')")
    @Log(title = "用户收藏商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserFavorites userFavorites) {
        List<UserFavorites> list = userFavoritesService.selectUserFavoritesList(userFavorites);
        ExcelUtil<UserFavorites> util = new ExcelUtil<UserFavorites>(UserFavorites.class);
        util.exportExcel(response, list, "用户收藏商品数据");
    }

    /**
     * 获取用户收藏商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:query')")
    @GetMapping(value = "/{userFavoId}")
    public AjaxResult getInfo(@PathVariable("userFavoId") Long userFavoId) {
        return success(userFavoritesService.selectUserFavoritesByUserFavoId(userFavoId));
    }

    /**
     * 新增用户收藏商品
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:add')")
    @Log(title = "用户收藏商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserFavorites userFavorites) {
        return toAjax(userFavoritesService.insertUserFavorites(userFavorites));
    }

    /**
     * 修改用户收藏商品
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:edit')")
    @Log(title = "用户收藏商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserFavorites userFavorites) {
        return toAjax(userFavoritesService.updateUserFavorites(userFavorites));
    }

    /**
     * 删除用户收藏商品
     */
    @PreAuthorize("@ss.hasPermi('system:favorites:remove')")
    @Log(title = "用户收藏商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userFavoIds}")
    public AjaxResult remove(@PathVariable Long[] userFavoIds) {
        return toAjax(userFavoritesService.deleteUserFavoritesByUserFavoIds(userFavoIds));
    }
}
