package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.web.controller.xkt.vo.advertRoundRecord.AdvertRoundRecordPageVO;
import com.ruoyi.xkt.dto.advertRoundRecord.AdvertRoundRecordPageDTO;
import com.ruoyi.xkt.dto.advertRoundRecord.AdvertRoundRecordPageResDTO;
import com.ruoyi.xkt.service.IAdvertRoundRecordService;
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
 * 推广轮次竞价失败Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "推广轮次竞价失败")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/ad-round-records")
public class AdvertRoundRecordController extends XktBaseController {

    final IAdvertRoundRecordService advertRoundRecordService;

    @PreAuthorize("@ss.hasAnyRoles('store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "获取档口竞价失败列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<AdvertRoundRecordPageResDTO>> page(@Validated @RequestBody AdvertRoundRecordPageVO pageVO) {
        return R.ok(advertRoundRecordService.page(BeanUtil.toBean(pageVO, AdvertRoundRecordPageDTO.class)));
    }

}
