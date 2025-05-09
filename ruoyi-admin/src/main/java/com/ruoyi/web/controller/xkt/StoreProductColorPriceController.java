package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.storeProdColorPrice.StoreProdColorPriceVO;
import com.ruoyi.xkt.service.IStoreProductColorPriceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    final IStoreProductColorPriceService prodColorPriceService;

    /**
     * 根据storeProdId获取所有颜色分类及定价
     */
    @ApiOperation(value = "根据storeProdId获取所有颜色分类及定价", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}/{storeProdId}")
    public R<List<StoreProdColorPriceVO>> getColorPriceByStoreProdId(@PathVariable(value = "storeProdId") Long storeProdId, @PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.copyToList(prodColorPriceService.getColorPriceByStoreProdId(storeId, storeProdId), StoreProdColorPriceVO.class));
    }

}
