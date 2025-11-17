package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.storeSaleDetail.StoreTodaySaleVO;
import com.ruoyi.web.controller.xkt.vo.storeSaleDetail.StoreTodaySaleSummaryVO;
import com.ruoyi.xkt.service.IStoreSaleDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 档口销售明细Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口销售出库明细")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/store-sale-details")
public class StoreSaleDetailController extends XktBaseController {

    final IStoreSaleDetailService saleDetailService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "查询档口今日销售额", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/today-sale/{storeId}")
    public R<StoreTodaySaleVO> getTodaySale(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(saleDetailService.getTodaySale(storeId), StoreTodaySaleVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "查询今日统计数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/today-sale/summary/{storeId}")
    public R<StoreTodaySaleSummaryVO> getTodaySaleSummary(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(saleDetailService.getTodaySaleSummary(storeId), StoreTodaySaleSummaryVO.class));
    }

}
