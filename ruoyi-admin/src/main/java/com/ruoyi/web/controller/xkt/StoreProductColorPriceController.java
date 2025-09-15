package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.web.controller.xkt.vo.storeProdColorPrice.StoreProdColorPricePageVO;
import com.ruoyi.web.controller.xkt.vo.storeProdColorPrice.StoreProdColorPriceVO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPricePageDTO;
import com.ruoyi.xkt.dto.storeProdColorPrice.StoreProdColorPriceResDTO;
import com.ruoyi.xkt.service.IStoreProductColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 档口商品颜色定价Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RequiredArgsConstructor
@Api(tags = "档口商品颜色定价")
@RestController
@RequestMapping("/rest/v1/prod-color-prices")
public class StoreProductColorPriceController extends XktBaseController {

    final IStoreProductColorService prodColorService;

    @ApiOperation(value = "根据storeProdId获取所有颜色分类及定价", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}/{storeProdId}")
    public R<List<StoreProdColorPriceVO>> getColorPriceByStoreProdId(@PathVariable(value = "storeProdId") Long storeProdId, @PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.copyToList(prodColorService.getColorPriceByStoreProdId(storeId, storeProdId), StoreProdColorPriceVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "获取商品颜色价格列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreProdColorPriceResDTO>> page(@Validated @RequestBody StoreProdColorPricePageVO pageVO) {
        return R.ok(prodColorService.page(BeanUtil.toBean(pageVO, StoreProdColorPricePageDTO.class)));
    }

}
