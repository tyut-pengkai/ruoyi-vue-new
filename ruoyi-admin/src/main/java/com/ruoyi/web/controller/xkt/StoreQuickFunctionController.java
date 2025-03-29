package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.vo.menu.SysMenuDTO;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.web.controller.xkt.vo.storeQuickFunction.StoreQuickFuncVO;
import com.ruoyi.xkt.domain.StoreQuickFunction;
import com.ruoyi.xkt.dto.storeQuickFunction.StoreQuickFuncDTO;
import com.ruoyi.xkt.service.IStoreQuickFunctionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 档口快捷功能Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/quick-functions")
public class StoreQuickFunctionController extends XktBaseController {

    final IStoreQuickFunctionService storeQuickFuncService;
    final ISysMenuService menuService;

    /**
     * 查看当前档口已绑定的所有快捷菜单
     */
    @PreAuthorize("@ss.hasPermi('system:function:list')")
    @GetMapping("/menus/{storeId}")
    public StoreQuickFuncVO getMenus(@PathVariable Long storeId) {
        // 找到当前档口所有的快捷菜单
        List<StoreQuickFuncDTO.DetailDTO> checkedList = storeQuickFuncService.getCheckedMenuList(storeId);
        // 找到系统所有的二级菜单
        List<SysMenuDTO> sysMenuList = menuService.selectMenuListByRoleIdAndMenuType(2L, "C");
        return StoreQuickFuncVO.builder().storeId(storeId)
                .checkedList(CollectionUtils.isEmpty(checkedList) ? new ArrayList<>() : BeanUtil.copyToList(checkedList, StoreQuickFuncVO.QuickFuncDetailVO.class))
                .menuList(CollectionUtils.isEmpty(sysMenuList) ? new ArrayList<>() : BeanUtil.copyToList(sysMenuList, StoreQuickFuncVO.QuickFuncDetailVO.class))
                .build();
    }

    /**
     * 修改档口快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:edit')")
    @Log(title = "档口快捷功能", businessType = BusinessType.UPDATE)
    @PutMapping("/checked")
    public R editCheckedList( @Validated  @RequestBody StoreQuickFuncVO quickFuncVO) {
        storeQuickFuncService.updateCheckedList(BeanUtil.toBean(quickFuncVO, StoreQuickFuncDTO.class));
        return success();
    }


    /**
     * 查询档口快捷功能列表
     */
    @PreAuthorize("@ss.hasPermi('system:function:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreQuickFunction storeQuickFunction) {
        startPage();
        List<StoreQuickFunction> list = storeQuickFuncService.selectStoreQuickFunctionList(storeQuickFunction);
        return getDataTable(list);
    }

    /**
     * 导出档口快捷功能列表
     */
    @PreAuthorize("@ss.hasPermi('system:function:export')")
    @Log(title = "档口快捷功能", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreQuickFunction storeQuickFunction) {
        List<StoreQuickFunction> list = storeQuickFuncService.selectStoreQuickFunctionList(storeQuickFunction);
        ExcelUtil<StoreQuickFunction> util = new ExcelUtil<StoreQuickFunction>(StoreQuickFunction.class);
        util.exportExcel(response, list, "档口快捷功能数据");
    }

    /**
     * 获取档口快捷功能详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:function:query')")
    @GetMapping(value = "/{storeQuickFuncId}")
    public R getInfo(@PathVariable("storeQuickFuncId") Long storeQuickFuncId) {
        return success(storeQuickFuncService.selectStoreQuickFunctionByStoreQuickFuncId(storeQuickFuncId));
    }

    /**
     * 新增档口快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:add')")
    @Log(title = "档口快捷功能", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreQuickFunction storeQuickFunction) {
        return success(storeQuickFuncService.insertStoreQuickFunction(storeQuickFunction));
    }

    /**
     * 修改档口快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:edit')")
    @Log(title = "档口快捷功能", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreQuickFunction storeQuickFunction) {
        return success(storeQuickFuncService.updateStoreQuickFunction(storeQuickFunction));
    }

    /**
     * 删除档口快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:remove')")
    @Log(title = "档口快捷功能", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeQuickFuncIds}")
    public R remove(@PathVariable Long[] storeQuickFuncIds) {
        return success(storeQuickFuncService.deleteStoreQuickFunctionByStoreQuickFuncIds(storeQuickFuncIds));
    }

}
