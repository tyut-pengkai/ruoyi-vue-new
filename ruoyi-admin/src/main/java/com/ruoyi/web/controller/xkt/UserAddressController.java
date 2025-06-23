package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.controller.xkt.vo.IdVO;
import com.ruoyi.web.controller.xkt.vo.express.UserAddressCreateVO;
import com.ruoyi.web.controller.xkt.vo.express.UserAddressInfoVO;
import com.ruoyi.web.controller.xkt.vo.express.UserAddressModifyVO;
import com.ruoyi.xkt.dto.express.UserAddressInfoDTO;
import com.ruoyi.xkt.service.IUserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户收货地址Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "用户收货地址")
@RestController
@RequestMapping("/rest/v1/user-address")
public class UserAddressController extends XktBaseController {
    @Autowired
    private IUserAddressService userAddressService;

    @PreAuthorize("@ss.hasAnyRoles('seller')")
    @ApiOperation("创建用户收货地址")
    @PostMapping("create")
    public R<UserAddressInfoVO> create(@Valid @RequestBody UserAddressCreateVO vo) {
        UserAddressInfoDTO dto = BeanUtil.toBean(vo, UserAddressInfoDTO.class);
        dto.setUserId(SecurityUtils.getUserId());
        return success(BeanUtil.toBean(userAddressService.createUserAddress(dto), UserAddressInfoVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('seller')")
    @ApiOperation("修改用户收货地址")
    @PostMapping("edit")
    public R<UserAddressInfoVO> edit(@Valid @RequestBody UserAddressModifyVO vo) {
        UserAddressInfoDTO dto = BeanUtil.toBean(vo, UserAddressInfoDTO.class);
        dto.setUserId(SecurityUtils.getUserId());
        userAddressService.checkOwner(dto.getId(), dto.getUserId());
        return success(BeanUtil.toBean(userAddressService.modifyUserAddress(dto), UserAddressInfoVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('seller')")
    @ApiOperation("复制用户收货地址")
    @PostMapping("copy")
    public R<UserAddressInfoVO> copy(@Valid @RequestBody IdVO vo) {
        userAddressService.checkOwner(vo.getId(), SecurityUtils.getUserId());
        UserAddressInfoDTO dto = userAddressService.copyUserAddress(vo.getId());
        return success(BeanUtil.toBean(dto, UserAddressInfoVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('seller')")
    @ApiOperation("删除用户收货地址")
    @PostMapping("delete")
    public R delete(@Valid @RequestBody IdVO vo) {
        userAddressService.checkOwner(vo.getId(), SecurityUtils.getUserId());
        userAddressService.deleteUserAddress(vo.getId());
        return success();
    }

    @PreAuthorize("@ss.hasAnyRoles('seller')")
    @ApiOperation(value = "用户收货地址详情")
    @GetMapping("/{id}")
    public R<UserAddressInfoVO> getInfo(@PathVariable("id") Long id) {
        userAddressService.checkOwner(id, SecurityUtils.getUserId());
        UserAddressInfoDTO infoDTO = userAddressService.getUserAddress(id);
        return success(BeanUtil.toBean(infoDTO, UserAddressInfoVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('seller')")
    @ApiOperation(value = "用户收货地址列表")
    @PostMapping("/list")
    public R<List<UserAddressInfoVO>> list() {
        List<UserAddressInfoDTO> dtoList = userAddressService.listByUser(SecurityUtils.getUserId());
        return success(BeanUtil.copyToList(dtoList, UserAddressInfoVO.class));
    }


}
