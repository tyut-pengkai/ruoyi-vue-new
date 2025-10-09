package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.web.controller.xkt.vo.quickFunction.QuickFuncResVO;
import com.ruoyi.web.controller.xkt.vo.quickFunction.QuickFuncUpdateVO;
import com.ruoyi.xkt.dto.quickFunction.QuickFuncUpdateDTO;
import com.ruoyi.xkt.service.IQuickFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,seller,agent,store')")
    @ApiOperation(value = "档口或电商卖家选择的快捷功能", httpMethod = "GET", response = R.class)
    @GetMapping("/selected")
    public R<QuickFuncResVO> getSelectedList() {
        // 找到当前所有的快捷菜单
        return R.ok(BeanUtil.toBean(quickFuncService.getCheckedMenuList(), QuickFuncResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,seller,agent,store')")
    @ApiOperation(value = "修改快捷功能", httpMethod = "PUT", response = R.class)
    @Log(title = "修改快捷功能", businessType = BusinessType.UPDATE)
    @PutMapping("/checked")
    public R<Integer> update(@Validated @RequestBody QuickFuncUpdateVO quickFuncVO) {
        return R.ok(quickFuncService.update(BeanUtil.toBean(quickFuncVO, QuickFuncUpdateDTO.class)));
    }

}
