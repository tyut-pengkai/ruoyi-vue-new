package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.storePordColor.StoreProdColorResVO;
import com.ruoyi.web.controller.xkt.vo.storePordColor.StoreProdColorSnResVO;
import com.ruoyi.web.controller.xkt.vo.storePordColor.StoreProductColorFuzzyPageVO;
import com.ruoyi.xkt.dto.storeProdColor.StoreProductColorFuzzyPageDTO;
import com.ruoyi.xkt.service.IStoreProductColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation(value = "模糊查询档口所有的商品颜色分类", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/fuzzy")
    public R<List<StoreProdColorResVO>> fuzzyQueryColorList(@RequestBody StoreProductColorFuzzyPageVO pageVO) {
        return success(BeanUtil.copyToList(storeProdColorService.fuzzyQueryColorList(BeanUtil.toBean(pageVO, StoreProductColorFuzzyPageDTO.class)), StoreProdColorResVO.class));
    }

    @ApiOperation(value = "根据商品ID查询颜色及已设置颜色条码", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/sn/{storeId}/{storeProdId}")
    public R<List<StoreProdColorSnResVO>> queryColorAndSetSnList(@PathVariable("storeId") Long storeId, @PathVariable("storeProdId") Long storeProdId) {
        return success(BeanUtil.copyToList(storeProdColorService.queryColorAndSetSnList(storeId, storeProdId), StoreProdColorSnResVO.class));
    }

}
