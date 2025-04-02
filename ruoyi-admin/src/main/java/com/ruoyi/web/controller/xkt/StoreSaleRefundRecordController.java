package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProdStock.StoreProdStockPageVO;
import com.ruoyi.web.controller.xkt.vo.storeSaleRefundRecord.StoreSaleRefundRecordVO;
import com.ruoyi.xkt.domain.StoreSaleRefundRecord;
import com.ruoyi.xkt.dto.storeProductStock.StoreProdStockPageDTO;
import com.ruoyi.xkt.dto.storeSaleRefundRecord.StoreSaleRefundRecordDTO;
import com.ruoyi.xkt.service.IStoreSaleRefundRecordService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 档口销售返单Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/sale-refund-records")
public class StoreSaleRefundRecordController extends XktBaseController {

    final IStoreSaleRefundRecordService storeSaleRefundRecordService;

    /**
     * 查询档口销售出库返单记录
     */
    @PreAuthorize("@ss.hasPermi('system:refund:list')")
    @ApiOperation(value = "查询档口销售出库返单记录", httpMethod = "POST", response = R.class)
    @GetMapping("/list/{storeId}/{storeSaleId}")
    public R<List<StoreSaleRefundRecordVO>> selectPage(@PathVariable("storeId") Long storeId, @PathVariable("storeSaleId") Long storeSaleId) {
        List<StoreSaleRefundRecordDTO> refundDTOList = this.storeSaleRefundRecordService.selectList(storeId, storeSaleId);
        return R.ok(BeanUtil.copyToList(refundDTOList, StoreSaleRefundRecordVO.class));
    }

}
