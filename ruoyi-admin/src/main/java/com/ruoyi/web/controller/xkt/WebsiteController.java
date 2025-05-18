package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.web.controller.xkt.vo.advertRound.pc.*;
import com.ruoyi.web.controller.xkt.website.IndexSearchVO;
import com.ruoyi.xkt.dto.es.ESProductDTO;
import com.ruoyi.xkt.dto.website.IndexSearchDTO;
import com.ruoyi.xkt.service.IWebsiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 网站首页Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "网站首页")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/website")
public class WebsiteController extends XktBaseController {

    final IWebsiteService websiteService;

    // TODO 通过定时任务 将广告位放到redis中
    // TODO 通过定时任务 将广告位放到redis中
    // TODO 通过定时任务 将广告位放到redis中

    @ApiOperation(value = "网站首页搜索", httpMethod = "POST", response = R.class)
    @PostMapping("/index/search")
    public R<Page<ESProductDTO>> page(@Validated @RequestBody IndexSearchVO searchVO) throws IOException {
        return R.ok(websiteService.search(BeanUtil.toBean(searchVO, IndexSearchDTO.class)));
    }

    @ApiOperation(value = "PC 首页 顶部横向轮播图", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/index/top/left")
    public R<List<PCIndexTopLeftBannerVO>> getPcIndexTopLeftBanner() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcIndexTopLeftBanner(), PCIndexTopLeftBannerVO.class));
    }

    @ApiOperation(value = "PC 首页 顶部纵向轮播图", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/index/top/right")
    public R<List<PCIndexTopRightBannerVO>> getPcIndexTopRightBanner() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcIndexTopRightBanner(), PCIndexTopRightBannerVO.class));
    }

    @ApiOperation(value = "PC 首页 销售榜", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/index/mid/sales")
    public R<List<PCIndexMidSalesVO>> getPcIndexMidSaleList() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcIndexMidSaleList(), PCIndexMidSalesVO.class));
    }

    @ApiOperation(value = "PC 首页 风格榜", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/index/mid/styles")
    public R<List<PCIndexMidStyleVO>> getPcIndexMidStyleList() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcIndexMidStyleList(), PCIndexMidStyleVO.class));
    }


    @ApiOperation(value = "PC 首页 人气榜", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/index/bottom/popular")
    public R<PCIndexBottomPopularVO> getPcIndexBottomPopularList() {
        return R.ok(BeanUtil.toBean(websiteService.getPcIndexBottomPopularList(), PCIndexBottomPopularVO.class));
    }




}
