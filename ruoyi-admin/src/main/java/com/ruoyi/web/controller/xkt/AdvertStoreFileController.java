package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.web.controller.xkt.vo.advertStoreFile.AdvertStoreFilePageVO;
import com.ruoyi.xkt.dto.advertStoreFile.AdvertStoreFilePageDTO;
import com.ruoyi.xkt.dto.advertStoreFile.AdvertStoreFileResDTO;
import com.ruoyi.xkt.service.IAdvertStoreFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员管理推广营销Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "推广营销图片管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/ad-store-file")
public class AdvertStoreFileController extends XktBaseController {

    final IAdvertStoreFileService advertStoreFileService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "查询图片管理列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<AdvertStoreFileResDTO>> page(@Validated @RequestBody AdvertStoreFilePageVO pageVO) {
        return R.ok(advertStoreFileService.page(BeanUtil.toBean(pageVO, AdvertStoreFilePageDTO.class)));
    }

}
