package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeHomepage.*;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeDecorationDTO;
import com.ruoyi.xkt.service.IStoreHomepageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 档口首页装修Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口首页装修")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-home")
public class StoreHomepageController extends XktBaseController {

    final IStoreHomepageService storeHomeService;

    @ApiOperation(value = "新增档口装修数据", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口装修数据", businessType = BusinessType.INSERT)
    @PostMapping("/decoration/{storeId}/{templateNum}")
    public R<Integer> addDecoration(@PathVariable("storeId") Long storeId, @PathVariable("templateNum") Integer templateNum,
                                    @Validated @RequestBody StoreHomeDecorationVO decorationVO) {
        return R.ok(storeHomeService.insert(storeId, templateNum, BeanUtil.toBean(decorationVO, StoreHomeDecorationDTO.class)));
    }

    @ApiOperation(value = "查询档口装修数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/decoration/{storeId}")
    public R<StoreHomeDecorationVO> getDecorationInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeHomeService.selectByStoreId(storeId), StoreHomeDecorationVO.class));
    }

    @ApiOperation(value = "修改档口装修数据", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口装修数据", businessType = BusinessType.UPDATE)
    @PutMapping("/decoration/{storeId}/{templateNum}")
    public R<Integer> editDecoration(@PathVariable("storeId") Long storeId, @PathVariable("templateNum") Integer templateNum,
                                     @Validated @RequestBody StoreHomeDecorationVO homepageVO) {
        return R.ok(storeHomeService.updateStoreHomepage(storeId, templateNum, BeanUtil.toBean(homepageVO, StoreHomeDecorationDTO.class)));
    }

    @ApiOperation(value = "获取档口推荐商品列表", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/recommend/{storeId}")
    public R<List<StoreRecommendResVO>> getStoreRecommendList(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.copyToList(storeHomeService.getStoreRecommendList(storeId), StoreRecommendResVO.class));
    }

    @ApiOperation(value = "获取档口首页 模板一 数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/template-one/{storeId}")
    public R<StoreHomeTemplateOneResVO> getTemplateOne(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeHomeService.getTemplateOne(storeId), StoreHomeTemplateOneResVO.class));
    }

    @ApiOperation(value = "获取档口首页 模板二 数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/template-two/{storeId}")
    public R<StoreHomeTemplateTwoResVO> getTemplateTwo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeHomeService.getTemplateTwo(storeId), StoreHomeTemplateTwoResVO.class));
    }

    @ApiOperation(value = "获取档口首页 模板三 数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/template-third/{storeId}")
    public R<StoreHomeTemplateThirdResVO> getTemplateThird(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeHomeService.getTemplateThird(storeId), StoreHomeTemplateThirdResVO.class));
    }

    @ApiOperation(value = "获取档口首页 模板四 数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/template-four/{storeId}")
    public R<StoreHomeTemplateFourResVO> getTemplateFour(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeHomeService.getTemplateFour(storeId), StoreHomeTemplateFourResVO.class));
    }

    @ApiOperation(value = "获取档口首页 模板五 数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/template-five/{storeId}")
    public R<StoreHomeTemplateFiveResVO> getTemplateFive(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeHomeService.getTemplateFive(storeId), StoreHomeTemplateFiveResVO.class));
    }

}
