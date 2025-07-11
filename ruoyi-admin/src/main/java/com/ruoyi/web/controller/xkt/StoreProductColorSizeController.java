package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeProdColorSize.*;
import com.ruoyi.xkt.dto.storeProdColorSize.StorePrintSnDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSnDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreStockTakingSnDTO;
import com.ruoyi.xkt.service.IStoreProductColorSizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 档口商品颜色的尺码Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "商品条码处理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/prod-color-sizes")
public class StoreProductColorSizeController extends XktBaseController {

    final IStoreProductColorSizeService prodColorSizeService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "[销售出库]根据条码查询商品信息", businessType = BusinessType.INSERT)
    @ApiOperation(value = "[销售出库]根据条码查询商品信息", httpMethod = "POST", response = R.class)
    @PostMapping("/sn/store-sale")
    public R<StoreSaleSnResVO> storeSaleSn(@Validated @RequestBody StoreSaleSnVO snVO) {
        return R.ok(BeanUtil.toBean(prodColorSizeService.storeSaleSn(BeanUtil.toBean(snVO, StoreSaleSnDTO.class)), StoreSaleSnResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "[商品入库]根据条码查询商品信息", businessType = BusinessType.INSERT)
    @ApiOperation(value = "[商品入库]根据条码查询商品信息", httpMethod = "POST", response = R.class)
    @PostMapping("/sn/storage")
    public R<StoreStorageSnResVO> storageSnList(@Validated @RequestBody StoreStorageSnVO snVO) {
        return R.ok(BeanUtil.toBean(prodColorSizeService.storageSnList(BeanUtil.toBean(snVO, StoreProdSnDTO.class)), StoreStorageSnResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "[库存盘点]根据条码查询商品信息", businessType = BusinessType.INSERT)
    @ApiOperation(value = "[库存盘点]根据条码查询商品信息", httpMethod = "POST", response = R.class)
    @PostMapping("/sn/stock")
    public R<StoreStockTakingSnResVO> stockTakingSnList(@Validated @RequestBody StoreStockTakingSnVO snVO) {
        return R.ok(BeanUtil.toBean(prodColorSizeService.stockTakingSnList(BeanUtil.toBean(snVO, StoreStockTakingSnDTO.class)), StoreStockTakingSnResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "[打印条码] 获取商品条码", businessType = BusinessType.INSERT)
    @ApiOperation(value = "[打印条码] 获取商品条码", httpMethod = "POST", response = R.class)
    @PostMapping("/sn/print")
    public R<List<StorePrintSnResVO>> getPrintSnList(@Validated @RequestBody StorePrintSnVO snVO) {
        return R.ok(BeanUtil.copyToList(prodColorSizeService.getPrintSnList(BeanUtil.toBean(snVO, StorePrintSnDTO.class)), StorePrintSnResVO.class));
    }

}
