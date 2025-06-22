package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.storeSaleRefundRecord.StoreSaleRefundRecordVO;
import com.ruoyi.xkt.dto.storeSaleRefundRecord.StoreSaleRefundRecordDTO;
import com.ruoyi.xkt.service.IStoreSaleRefundRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 档口销售返单Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口销售返单")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/sale-refund-records")
public class StoreSaleRefundRecordController extends XktBaseController {

    final IStoreSaleRefundRecordService storeSaleRefundRecordService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "查询档口销售出库返单记录", httpMethod = "POST", response = R.class)
    @GetMapping("/list/{storeId}/{storeSaleId}")
    public R<List<StoreSaleRefundRecordVO>> selectPage(@PathVariable("storeId") Long storeId, @PathVariable("storeSaleId") Long storeSaleId) {
        List<StoreSaleRefundRecordDTO> refundDTOList = this.storeSaleRefundRecordService.selectList(storeId, storeSaleId);
        return R.ok(BeanUtil.copyToList(refundDTOList, StoreSaleRefundRecordVO.class));
    }

}
