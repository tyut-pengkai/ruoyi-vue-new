package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.UserInfo;
import com.ruoyi.common.core.domain.model.UserInfoEdit;
import com.ruoyi.common.core.domain.model.UserListItem;
import com.ruoyi.common.core.domain.model.UserQuery;
import com.ruoyi.common.core.page.PageVO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.system.vo.*;
import com.ruoyi.web.controller.xkt.vo.IdsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/rest/v1/sys/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private TokenService tokenService;

    @ApiOperation(value = "用户分页查询 - 管理员")
    @PostMapping("/page")
    public R<PageVO<UserListItemVO>> page(@Validated @RequestBody UserQueryVO vo) {
        UserQuery query = BeanUtil.toBean(vo, UserQuery.class);
        Page<UserListItem> page = PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        userService.listUser(query);
        return R.ok(PageVO.of(page, UserListItemVO.class));
    }

    @ApiOperation(value = "用户分页查询 - 档口")
    @PostMapping("/store/page")
    public R<PageVO<UserListItemVO>> pageByStore(@Validated @RequestBody UserQueryVO vo) {
        UserQuery query = BeanUtil.toBean(vo, UserQuery.class);
        // 只能查询当前档口
        query.setStoreIds(Collections.singletonList(SecurityUtils.getStoreId()));
        Page<UserListItem> page = PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        userService.listUser(query);
        return R.ok(PageVO.of(page, UserListItemVO.class));
    }

    @ApiOperation(value = "用户详情")
    @GetMapping(value = "/{id}")
    public R<UserInfoVO> getInfo(@PathVariable("id") Long id) {
        UserInfo infoDTO = userService.getUserById(id);
        UserInfoVO vo = BeanUtil.toBean(infoDTO, UserInfoVO.class);
        vo.setRoleIds(CollUtil.emptyIfNull(vo.getRoles()).stream().map(RoleInfoVO::getRoleId)
                .collect(Collectors.toList()));
        return R.ok(vo);
    }

    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @ApiOperation("创建用户")
    @PostMapping("create")
    public R<Long> create(@Valid @RequestBody UserInfoEditVO vo) {
        UserInfoEdit dto = BeanUtil.toBean(vo, UserInfoEdit.class);
        dto.setUserId(null);
        Long userId = userService.createUser(dto);
        return R.ok(userId);
    }

    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改用户")
    @PostMapping("edit")
    public R<Long> edit(@Valid @RequestBody UserInfoEditVO vo) {
        Assert.notNull(vo.getUserId(), "用户ID不能为空");
        UserInfoEdit dto = BeanUtil.toBean(vo, UserInfoEdit.class);
        Long userId = userService.updateUser(dto);
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(userId);
        return R.ok(userId);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @ApiOperation("导出")
    @PostMapping("/export")
    public void export(@Validated @RequestBody UserQueryVO vo, HttpServletResponse response) {
        UserQuery query = BeanUtil.toBean(vo, UserQuery.class);
        List<UserListItem> list = userService.listUser(query);
        ExcelUtil<UserListItem> util = new ExcelUtil<>(UserListItem.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @ApiOperation("导入")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @ApiOperation("导入模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }


    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @ApiOperation("删除用户")
    @PostMapping("/remove")
    public R<Integer> remove(@Validated @RequestBody IdsVO vo) {
        int count = userService.batchDeleteUser(vo.getIds());
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(vo.getIds());
        return R.ok(count);
    }

    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改用户状态")
    @PostMapping("/changeStatus")
    public R<Integer> changeStatus(@Validated @RequestBody BatchOptStatusVO vo) {
        int count = userService.batchUpdateUserStatus(vo.getIds(), vo.getStatus());
        if (!Constants.SYS_NORMAL_STATUS.equals(vo.getStatus())) {
            // 清除用户缓存（退出登录）
            tokenService.deleteCacheUser(vo.getIds());
        }
        return R.ok(count);
    }

    /**
     * 重置密码
     */
    @ApiOperation("重置密码")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    public R resetPwd(@Validated @RequestBody PwdResetVO vo) {
        userService.resetPassword(vo.getId(), vo.getNewPwd());
        return R.ok();
    }
}
