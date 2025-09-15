/*
package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeRoleAccount.*;
import com.ruoyi.xkt.dto.storeRoleAccount.StoreRoleAccDTO;
import com.ruoyi.xkt.dto.storeRoleAccount.StoreRoleAccListDTO;
import com.ruoyi.xkt.dto.storeRoleAccount.StoreRoleAccUpdateDTO;
import com.ruoyi.xkt.dto.storeRoleAccount.StoreRoleAccUpdateStatusDTO;
import com.ruoyi.xkt.service.IStoreRoleAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

*/
/**
 * 档口子账号Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 * <p>
 * 新增档口子账号
 * <p>
 * 修改档口子账号
 * <p>
 * 获取档口子账号列表
 * <p>
 * 获取档口子账号详情
 * <p>
 * 停用/启用档口子账号
 * <p>
 * 新增档口子账号
 * <p>
 * 修改档口子账号
 * <p>
 * 获取档口子账号列表
 * <p>
 * 获取档口子账号详情
 * <p>
 * 停用/启用档口子账号
 *//*

@Api(tags = "档口子账号")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-accounts")
public class StoreRoleAccountController extends XktBaseController {

    final IStoreRoleAccountService storeRoleAccService;

    */
/**
 * 新增档口子账号
 *//*

    // @PreAuthorize("@ss.hasPermi('system:account:add')")
    @ApiOperation(value = "新增档口子账号", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口子账号", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody StoreRoleAccVO roleAccVO) {
        return R.ok(storeRoleAccService.insert(BeanUtil.toBean(roleAccVO, StoreRoleAccDTO.class)));
    }

    */
/**
 * 修改档口子账号
 *//*

    // @PreAuthorize("@ss.hasPermi('system:account:edit')")
    @ApiOperation(value = "修改档口子账号", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口子账号", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreRoleAccUpdateVO accUpdateVO) {
        return R.ok(storeRoleAccService.update(BeanUtil.toBean(accUpdateVO, StoreRoleAccUpdateDTO.class)));
    }

    */
/**
 * 获取档口子账号列表
 *//*

    // @PreAuthorize("@ss.hasPermi('system:role:list')")
    @ApiOperation(value = "获取档口子账号列表", httpMethod = "POST", response = R.class)
    @PostMapping("/list")
    public R<List<StoreRoleAccResVO>> list(@Validated @RequestBody StoreRoleAccListVO accListVO) {
        return R.ok(BeanUtil.copyToList(storeRoleAccService.list(BeanUtil.toBean(accListVO, StoreRoleAccListDTO.class)), StoreRoleAccResVO.class));
    }

    */
/**
 * 获取档口子账号详情
 *//*

    // @PreAuthorize("@ss.hasPermi('system:account:query')")
    @ApiOperation(value = "获取档口子账号详情", httpMethod = "POST", response = R.class)
    @GetMapping(value = "/{storeRoleAccId}")
    public R<StoreRoleAccDetailResVO> getInfo(@PathVariable("storeRoleAccId") Long storeRoleAccId) {
        return R.ok(BeanUtil.toBean(storeRoleAccService.selectByStoreRoleAccId(storeRoleAccId), StoreRoleAccDetailResVO.class));
    }

    */
/**
 * 停用/启用档口子账号
 *//*

    // @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @ApiOperation(value = "停用/启用档口子账号", httpMethod = "PUT", response = R.class)
    @Log(title = "停用/启用档口子账号", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public R<Integer> updateAccountStatus(@Validated @RequestBody StoreRoleAccUpdateStatusVO accUpdateStatusVO) {
        return R.ok(storeRoleAccService.updateAccountStatus(BeanUtil.toBean(accUpdateStatusVO, StoreRoleAccUpdateStatusDTO.class)));
    }

}
*/
