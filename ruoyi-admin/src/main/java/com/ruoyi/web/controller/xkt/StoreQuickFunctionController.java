package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.vo.menu.SysMenuDTO;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.web.controller.xkt.vo.storeQuickFunction.StoreQuickFuncVO;
import com.ruoyi.xkt.dto.storeQuickFunction.StoreQuickFuncDTO;
import com.ruoyi.xkt.service.IStoreQuickFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 档口快捷功能Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口快捷功能")
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
    @ApiOperation(value = "查看当前档口已绑定的所有快捷菜单", httpMethod = "GET", response = R.class)
    @GetMapping("/menus/{storeId}")
    public R<StoreQuickFuncVO> getMenus(@PathVariable Long storeId) {
        // 找到当前档口所有的快捷菜单
        List<StoreQuickFuncDTO.DetailDTO> checkedList = storeQuickFuncService.getCheckedMenuList(storeId);
        // 找到系统所有的二级菜单
        List<SysMenuDTO> sysMenuList = menuService.selectMenuListByRoleIdAndMenuType(2L, "C");
        return R.ok(StoreQuickFuncVO.builder().storeId(storeId)
                .checkedList(CollectionUtils.isEmpty(checkedList) ? new ArrayList<>() : BeanUtil.copyToList(checkedList, StoreQuickFuncVO.QuickFuncDetailVO.class))
                .menuList(CollectionUtils.isEmpty(sysMenuList) ? new ArrayList<>() : BeanUtil.copyToList(sysMenuList, StoreQuickFuncVO.QuickFuncDetailVO.class))
                .build());
    }

    /**
     * 修改档口快捷功能
     */
    @PreAuthorize("@ss.hasPermi('system:function:edit')")
    @ApiOperation(value = "修改档口快捷功能", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口快捷功能", businessType = BusinessType.UPDATE)
    @PutMapping("/checked")
    public R editCheckedList(@Validated @RequestBody StoreQuickFuncVO quickFuncVO) {
        storeQuickFuncService.updateCheckedList(BeanUtil.toBean(quickFuncVO, StoreQuickFuncDTO.class));
        return success();
    }


}
