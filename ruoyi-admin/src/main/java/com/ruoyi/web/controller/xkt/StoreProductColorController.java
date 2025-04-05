package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.storePordColor.StoreProdColorResVO;
import com.ruoyi.xkt.service.IStoreProductColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 档口当前商品颜色Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口商品颜色")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/prod-colors")
public class StoreProductColorController extends XktBaseController {

    final IStoreProductColorService storeProdColorService;

    /**
     * 模糊查询档口所有的商品颜色分类
     */
    @PreAuthorize("@ss.hasPermi('system:color:query')")
    @ApiOperation(value = "模糊查询档口所有的商品颜色分类", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/fuzzy")
    public R<StoreProdColorResVO> fuzzyQueryColorList(@RequestParam(value = "prodArtNum", required = false) String prodArtNum,
                                                      @RequestParam("storeId") Long storeId) {
        return success(BeanUtil.copyToList(storeProdColorService.fuzzyQueryColorList(storeId, prodArtNum), StoreProdColorResVO.class));
    }

}
