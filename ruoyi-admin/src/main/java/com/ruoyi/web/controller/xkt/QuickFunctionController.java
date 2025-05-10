package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.vo.menu.SysMenuDTO;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.web.controller.xkt.vo.quickFunction.QuickFuncVO;
import com.ruoyi.xkt.dto.quickFunction.QuickFuncDTO;
import com.ruoyi.xkt.service.IQuickFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 快捷功能Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "快捷功能")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/quick-functions")
public class QuickFunctionController extends XktBaseController {

    final IQuickFunctionService quickFuncService;
    final ISysMenuService menuService;

    private static final String MENU_TYPE_C = "C";

    /**
     * 查看已绑定的所有快捷菜单
     */
    // @PreAuthorize("@ss.hasPermi('system:function:list')")
    @ApiOperation(value = "查看已绑定的所有快捷菜单", httpMethod = "GET", response = R.class)
    @GetMapping("/menus/{roleId}/{bizId}")
    public R<QuickFuncVO> getMenuList(@PathVariable("roleId") Long roleId, @PathVariable("bizId") Long bizId) {
        // 找到当前所有的快捷菜单
        List<QuickFuncDTO.DetailDTO> checkedList = quickFuncService.getCheckedMenuList(roleId, bizId);
        // 找到系统所有的二级菜单
        List<SysMenuDTO> sysMenuList = menuService.selectMenuListByRoleIdAndMenuType(roleId, MENU_TYPE_C);
        return R.ok(QuickFuncVO.builder().bizId(bizId).roleId(roleId)
                .checkedList(BeanUtil.copyToList(checkedList, QuickFuncVO.QuickFuncDetailVO.class))
                .menuList(BeanUtil.copyToList(sysMenuList, QuickFuncVO.QuickFuncDetailVO.class))
                .build());
    }

    /**
     * 修改快捷功能
     */
    // @PreAuthorize("@ss.hasPermi('system:function:edit')")
    @ApiOperation(value = "修改快捷功能", httpMethod = "PUT", response = R.class)
    @Log(title = "修改快捷功能", businessType = BusinessType.UPDATE)
    @PutMapping("/checked")
    public R editCheckedList(@Validated @RequestBody QuickFuncVO quickFuncVO) {
        quickFuncService.updateCheckedList(BeanUtil.toBean(quickFuncVO, QuickFuncDTO.class));
        return success();
    }


}
