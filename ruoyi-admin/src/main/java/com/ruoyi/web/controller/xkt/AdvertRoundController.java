package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.advertRound.*;
import com.ruoyi.xkt.dto.advertRound.*;
import com.ruoyi.xkt.service.IAdvertRoundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

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
    public R<Integer> create(@Validated @RequestBody AdRoundStoreCreateVO createVO) throws ParseException {
        return R.ok(advertRoundService.create(BeanUtil.toBean(createVO, AdRoundStoreCreateDTO.class)));
    }

    /**
     * 获取推广位数据及右侧 "已订购推广位"列表
     */
    @ApiOperation(value = "获取推广位数据及右侧 已订购推广位 列表", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{advertId}/{storeId}/{showType}")
    public R<AdRoundStoreResVO> getStoreAdInfo(@PathVariable("advertId") Long advertId, @PathVariable("storeId") Long storeId, @PathVariable("showType") Integer showType) {
        return R.ok(BeanUtil.toBean(advertRoundService.getStoreAdInfo(storeId, advertId, showType), AdRoundStoreResVO.class));
    }

    /**
     * 获取当前最新的出价及设置的商品
     */
    @ApiOperation(value = "获取当前最新的出价及设置的商品", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/latest")
    public R<AdRoundLatestResVO> getLatestInfo(@Validated @RequestBody AdRoundLatestVO latestVO) {
        return R.ok(BeanUtil.toBean(advertRoundService.getLatestInfo(BeanUtil.toBean(latestVO, AdRoundLatestDTO.class)), AdRoundLatestResVO.class));
    }

    /**
     * 位置枚举类型的推广位，获取推广商品
     */
    @ApiOperation(value = "位置枚举类型的推广位，获取推广商品", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/position/{advertRoundId}")
    public R<List<AdRoundSetProdResVO>> getSetProdInfo(@PathVariable("advertRoundId") Long advertRoundId) {
        return R.ok(BeanUtil.copyToList(advertRoundService.getSetProdInfo(advertRoundId), AdRoundSetProdResVO.class));
    }

    /**
     * 获取档口已购推广列表
     */
    @ApiOperation(value = "获取档口已购推广列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<AdvertRoundStorePageResDTO>> page(@Validated @RequestBody AdvertRoundStorePageVO pageVO) {
        return R.ok(advertRoundService.page(BeanUtil.toBean(pageVO, AdvertRoundStorePageDTO.class)));
    }

    /**
     * 查看当前广告位推广图
     */
    @ApiOperation(value = "查看当前广告位推广图", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/pic/{storeId}/{advertRoundId}")
    public R<AdRoundStoreSetResVO> getAdvertStoreSetInfo(@PathVariable("storeId") Long storeId, @PathVariable("advertRoundId") Long advertRoundId) {
        return R.ok(BeanUtil.toBean(advertRoundService.getAdvertStoreSetInfo(storeId, advertRoundId), AdRoundStoreSetResVO.class));
    }

    /**
     * 退订
     */
    @ApiOperation(value = "退订", httpMethod = "POST", response = R.class)
    @Log(title = "退订", businessType = BusinessType.UPDATE)
    @PutMapping("/unsubscribe/{storeId}/{advertRoundId}")
    public R<Integer> unsubscribe(@PathVariable("storeId") Long storeId, @PathVariable("advertRoundId") Long advertRoundId) {
        return R.ok(advertRoundService.unsubscribe(storeId, advertRoundId));
    }

    /**
     * 取最受欢迎的8个推广位
     */
    @ApiOperation(value = "取最受欢迎的8个推广位", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/populars")
    public R<List<AdRoundPopularResVO>> getMostPopulars() {
        return R.ok(BeanUtil.copyToList(advertRoundService.getMostPopulars(), AdRoundPopularResVO.class));
    }

    /**
     * 档口上传推广图
     */
    @ApiOperation(value = "档口上传推广图", httpMethod = "PUT", response = R.class)
    @PutMapping("/upload/pic")
    public R<Integer> uploadAdvertPic(@Valid @RequestBody AdRoundUploadPicVO uploadPicVO) {
        return R.ok(advertRoundService.uploadAdvertPic(BeanUtil.toBean(uploadPicVO, AdRoundUploadPicDTO.class)));
    }

    /**
     * 查看图片审核拒绝理由
     */
    @ApiOperation(value = "查看图片审核拒绝理由", httpMethod = "PUT", response = R.class)
    @PutMapping("/reject-reason/{advertRoundId}")
    public R<String> getRejectReason(@PathVariable Long advertRoundId) {
        return R.ok(advertRoundService.getRejectReason(advertRoundId));
    }


}
