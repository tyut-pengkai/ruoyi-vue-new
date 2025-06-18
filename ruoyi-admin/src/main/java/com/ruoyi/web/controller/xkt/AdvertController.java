package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.advert.*;
import com.ruoyi.xkt.dto.advert.*;
import com.ruoyi.xkt.service.IAdvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员管理推广营销Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "推广营销")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/ads")
public class AdvertController extends XktBaseController {

    final IAdvertService advertService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "新增推广营销", httpMethod = "POST", response = R.class)
    @Log(title = "新增推广营销", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody AdvertCreateVO createVO) {
        return R.ok(advertService.create(BeanUtil.toBean(createVO, AdvertCreateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "获取推广营销详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{advertId}")
    public R<AdvertResVO> getInfo(@PathVariable("advertId") Long advertId) {
        return R.ok(BeanUtil.toBean(advertService.getInfo(advertId), AdvertResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "查询推广营销列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<AdvertResDTO>> page(@Validated @RequestBody AdvertPageVO pageVO) {
        return R.ok(advertService.page(BeanUtil.toBean(pageVO, AdvertPageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "修改推广营销信息", httpMethod = "PUT", response = R.class)
    @Log(title = "修改推广营销信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody AdvertUpdateVO updateVO) {
        return R.ok(advertService.updateAdvert(BeanUtil.toBean(updateVO, AdvertUpdateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "上线/下线 营销推广", httpMethod = "PUT", response = R.class)
    @Log(title = "上线/下线 营销推广", businessType = BusinessType.UPDATE)
    @PutMapping("/change-status")
    public R<Integer> changeAdvertStatus(@Validated @RequestBody AdvertChangeStatusVO changeStatusVO) {
        return R.ok(advertService.changeAdvertStatus(BeanUtil.toBean(changeStatusVO, AdvertChangeStatusDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "档口营销推广初始化数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/platform-list")
    public R<List<AdvertPlatformResVO>> getPlatformList() {
        return R.ok(BeanUtil.copyToList(advertService.getPlatformList(), AdvertPlatformResVO.class));
    }

    @ApiOperation(value = "获取推广位示例图", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/demo/{advertType}")
    public R<String> getDemoPic(@PathVariable Integer advertType) {
        return R.ok(advertService.getDemoPic(advertType));
    }


}
