package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.web.controller.xkt.vo.advertRound.pc.index.*;
import com.ruoyi.web.controller.xkt.vo.advertRound.pc.newArrival.*;
import com.ruoyi.web.controller.xkt.vo.advertRound.pc.store.PCStoreMidBannerVO;
import com.ruoyi.web.controller.xkt.vo.advertRound.pc.store.PCStoreTopBannerVO;
import com.ruoyi.web.controller.xkt.vo.advertRound.picSearch.PicSearchAdvertVO;
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

    @ApiOperation(value = "PC 首页 两侧固定挂耳", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/index/fixed-ear")
    public R<PCIndexFixedEarVO> getPcIndexFixedEar() {
        return R.ok(BeanUtil.toBean(websiteService.getPcIndexFixedEar(), PCIndexFixedEarVO.class));
    }

    @ApiOperation(value = "PC 首页 搜索框下档口名称", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/index/search-underline-store-name")
    public R<List<PCIndexSearchUnderlineStoreNameVO>> getPcIndexSearchUnderlineStoreName() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcIndexSearchUnderlineStoreName(), PCIndexSearchUnderlineStoreNameVO.class));
    }

    @ApiOperation(value = "PC 首页 搜索框中推荐商品", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/index/search-recommend-prod")
    public R<List<PCIndexSearchRecommendProdVO>> getPcIndexSearchRecommendProd() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcIndexSearchRecommendProd(), PCIndexSearchRecommendProdVO.class));
    }

    @ApiOperation(value = "PC 新品馆 顶部横向轮播图", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/new/top/left")
    public R<List<PCNewTopLeftBannerVO>> getPcNewTopLeftBanner() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcNewTopLeftBanner(), PCNewTopLeftBannerVO.class));
    }

    @ApiOperation(value = "PC 新品馆 顶部纵向图", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/new/top/right")
    public R<PCNewTopRightVO> getPcNewTopRight() {
        return R.ok(BeanUtil.toBean(websiteService.getPcNewTopRight(), PCNewTopRightVO.class));
    }

    @ApiOperation(value = "PC 新品馆 品牌榜", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/new/mid/brand")
    public R<List<PCNewMidBrandVO>> getPcNewMidBrandList() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcNewMidBrandList(), PCNewMidBrandVO.class));
    }

    @ApiOperation(value = "PC 新品馆 热卖榜左侧轮播图", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/new/mid/hot-left")
    public R<List<PCNewMidHotLeftVO>> getPcNewMidHotLeftList() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcNewMidHotLeftList(), PCNewMidHotLeftVO.class));
    }

    @ApiOperation(value = "PC 新品馆 热卖榜右侧商品图", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/new/mid/hot-right")
    public R<List<PCNewMidHotRightVO>> getPcNewMidHotRightList() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcNewMidHotRightList(), PCNewMidHotRightVO.class));
    }

    @ApiOperation(value = "PC 新品馆 横幅", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/new/bottom/banner")
    public R<List<PCNewBottomBannerVO>> getPcNewBottomBannerList() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcNewBottomBannerList(), PCNewBottomBannerVO.class));
    }

    @ApiOperation(value = "PC 档口馆 顶部banner", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/store/top/banner")
    public R<List<PCStoreTopBannerVO>> getPcStoreTopBannerList() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcStoreTopBannerList(), PCStoreTopBannerVO.class));
    }

    @ApiOperation(value = "PC 档口馆 中间横幅", httpMethod = "GET", response = R.class)
    @GetMapping("/pc/store/mid/banner")
    public R<List<PCStoreMidBannerVO>> getPcStoreMidBannerList() {
        return R.ok(BeanUtil.copyToList(websiteService.getPcStoreMidBannerList(), PCStoreMidBannerVO.class));
    }

    @ApiOperation(value = "以图搜款推广", httpMethod = "GET", response = R.class)
    @GetMapping("/pic-search")
    public R<List<PicSearchAdvertVO>> getPicSearchList() {
        return R.ok(BeanUtil.copyToList(websiteService.getPicSearchList(), PicSearchAdvertVO.class));
    }



}
