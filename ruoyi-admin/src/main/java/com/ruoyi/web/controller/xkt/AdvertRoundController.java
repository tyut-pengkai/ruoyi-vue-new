package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.advertRound.AdRoundStoreCreateVO;
import com.ruoyi.web.controller.xkt.vo.advertRound.AdRoundStoreResVO;
import com.ruoyi.xkt.dto.advertRound.AdRoundStoreCreateDTO;
import com.ruoyi.xkt.service.IAdvertRoundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * 推广营销轮次投放Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "推广营销轮次投放")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/ad-rounds")
public class AdvertRoundController extends XktBaseController {

    final IAdvertRoundService advertRoundService;

    /**
     * 档口购买推广营销
     */
    @ApiOperation(value = "档口购买推广营销", httpMethod = "POST", response = R.class)
    @Log(title = "档口购买推广营销", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody AdRoundStoreCreateVO createVO) {
        return R.ok(advertRoundService.create(BeanUtil.toBean(createVO, AdRoundStoreCreateDTO.class)));
    }


    /**
     * 根据类型查询当前档口的推广营销数据
     */
    @ApiOperation(value = "根据类型查询当前档口的推广营销数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{typeId}/{storeId}")
    public R<AdRoundStoreResVO> getStoreAdInfo(@PathVariable("typeId") Integer typeId,
                                               @PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(advertRoundService.getStoreAdInfo(storeId, typeId), AdRoundStoreResVO.class));
    }


    @GetMapping("/test")
    public void test() throws ParseException {
        advertRoundService.saveAdvertDeadlineToRedis();
    }



    // TODO 退订

    // TODO 每晚定时任务 调整 推广营销的offerStatus



    // TODO 获取最受欢迎推广位8个，固定不动了
    // TODO 获取最受欢迎推广位8个，固定不动了









}
