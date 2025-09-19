package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeProductDemandDetail.StoreProdDemandDetailUpdateVO;
import com.ruoyi.xkt.dto.storeProductDemandDetail.StoreProdDemandDetailUpdateDTO;
import com.ruoyi.xkt.service.IStoreProductDemandDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 档口商品需求单明细Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口商品需求单明细")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/prod-demand-details")
public class StoreProductDemandDetailController extends XktBaseController {

    final IStoreProductDemandDetailService demandDetailService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "编辑商品需求明细数量", httpMethod = "PUT", response = R.class)
    @Log(title = "编辑商品需求明细数量", businessType = BusinessType.UPDATE)
    @PutMapping("/quantity")
    public R<Integer> updateDetailQuantity(@Validated @RequestBody StoreProdDemandDetailUpdateVO updateVO) {
        return R.ok(demandDetailService.updateDetailQuantity(BeanUtil.toBean(updateVO, StoreProdDemandDetailUpdateDTO.class)));
    }


}
