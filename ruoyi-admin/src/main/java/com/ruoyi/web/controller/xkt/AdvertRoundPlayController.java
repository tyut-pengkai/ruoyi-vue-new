package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.advert.AdvertCreateVO;
import com.ruoyi.web.controller.xkt.vo.advertRoundPlay.AdPlayStoreCreateVO;
import com.ruoyi.web.controller.xkt.vo.advertRoundPlay.AdPlayStoreResVO;
import com.ruoyi.xkt.dto.advert.AdvertCreateDTO;
import com.ruoyi.xkt.dto.advertRoundPlay.AdPlayStoreCreateDTO;
import com.ruoyi.xkt.service.IAdvertRoundPlayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 推广营销轮次投放Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "推广营销轮次投放")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/ad-round-plays")
public class AdvertRoundPlayController extends XktBaseController {

    final IAdvertRoundPlayService adPlayService;


    /**
     * 档口购买推广营销
     */
    @ApiOperation(value = "档口购买推广营销", httpMethod = "POST", response = R.class)
    @Log(title = "档口购买推广营销", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody AdPlayStoreCreateVO createVO) {
        return R.ok(adPlayService.create(BeanUtil.toBean(createVO, AdPlayStoreCreateDTO.class)));
    }



    /**
     * 根据类型查询当前档口的推广营销数据
     */
    @ApiOperation(value = "根据类型查询当前档口的推广营销数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{typeId}/{storeId}")
    public R<AdPlayStoreResVO> getStoreAdInfo(@PathVariable("typeId") Integer typeId,
                                              @PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(adPlayService.getStoreAdInfo(storeId, typeId), AdPlayStoreResVO.class));
    }





    // TODO 获取最受欢迎推广位8个，固定不动了
    // TODO 获取最受欢迎推广位8个，固定不动了









}
