package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeProdColorSize.StoreSaleSnResVO;
import com.ruoyi.web.controller.xkt.vo.storeProdColorSize.StoreProdSnVO;
import com.ruoyi.web.controller.xkt.vo.storeProdColorSize.StoreSaleSnVO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreProdSnDTO;
import com.ruoyi.xkt.dto.storeProdColorSize.StoreSaleSnDTO;
import com.ruoyi.xkt.service.IStoreProductColorSizeService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 档口商品颜色的尺码Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@ApiModel(value = "商品条码处理")
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
    @Log(title = "[商品入库、库存盘点]根据条码查询商品信息", businessType = BusinessType.INSERT)
    @ApiOperation(value = "[商品入库、库存盘点]根据条码查询商品信息", httpMethod = "POST", response = R.class)
    @PostMapping("/sn")
    public R<StoreSaleSnResVO> sn(@Validated @RequestBody StoreProdSnVO snVO) {
        return R.ok(BeanUtil.toBean(prodColorSizeService.sn(BeanUtil.toBean(snVO, StoreProdSnDTO.class)), StoreSaleSnResVO.class));
    }




    // TODO 档口扫描货号，获取商品信息
    // TODO 档口扫描货号，获取商品信息
    // TODO 档口扫描货号，获取商品信息


    // TODO 打印条码
    // TODO 打印条码
    // TODO 打印条码

}
