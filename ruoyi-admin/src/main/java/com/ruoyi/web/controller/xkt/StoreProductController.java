package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProd.*;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.dto.storeProduct.StoreProdDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageResDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdStatusDTO;
import com.ruoyi.xkt.service.IStoreProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 档口商品Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prods")
@RequiredArgsConstructor
@Api(tags = "档口商品")
public class StoreProductController extends XktBaseController {

    final IStoreProductService storeProdService;

    /**
     * 查询档口商品所有的风格
     */
    @ApiOperation(value = "查询档口商品所有的风格", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/styles")
    public R<List<String>> getStyleList() {
        return R.ok(storeProdService.getStyleList());
    }

    /**
     * 模糊查询档口商品
     */
//    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @ApiOperation(value = "模糊查询档口商品", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/fuzzy")
    public R<List<StoreProdFuzzyResVO>> fuzzyQueryColorList(@RequestParam(value = "prodArtNum", required = false) String prodArtNum,
                                                            @RequestParam("storeId") Long storeId) {
        return R.ok(BeanUtil.copyToList(storeProdService.fuzzyQueryList(storeId, prodArtNum), StoreProdFuzzyResVO.class));
    }

    /**
     * 模糊查询档口商品
     */
//    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @ApiOperation(value = "模糊查询档口商品", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/fuzzy/pic")
    public R<List<StoreProdFuzzyResPicVO>> fuzzyQueryResPicList(@RequestParam(value = "prodArtNum", required = false) String prodArtNum,
                                                                @RequestParam("storeId") Long storeId) {
        return R.ok(BeanUtil.copyToList(storeProdService.fuzzyQueryResPicList(storeId, prodArtNum), StoreProdFuzzyResPicVO.class));
    }


    /**
     * 查询档口商品列表
     */
//    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @ApiOperation(value = "查询档口商品列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreProdPageResDTO>> page(@Validated @RequestBody StoreProdPageVO pageVO) {
        return R.ok(storeProdService.page(BeanUtil.toBean(pageVO, StoreProdPageDTO.class)));
    }

    /**
     * 获取档口商品详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @ApiOperation(value = "获取档口商品详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/detail/{storeProdId}")
    public R<StoreProdResVO> getInfo(@PathVariable("storeProdId") Long storeProdId) {
        return R.ok(BeanUtil.toBean(storeProdService.selectStoreProductByStoreProdId(storeProdId), StoreProdResVO.class));
    }

    /**
     * APP获取档口商品详细信息
     */
    @ApiOperation(value = "APP获取档口商品详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/app/detail/{storeProdId}")
    public R<StoreProdAppResVO> getAppInfo(@PathVariable("storeProdId") Long storeProdId) {
        return R.ok(BeanUtil.toBean(storeProdService.getAppInfo(storeProdId), StoreProdAppResVO.class));
    }


    /**
     * 获取档口商品颜色及sku等
     */
    @ApiOperation(value = "获取档口商品颜色及sku等", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/sku/{storeProdId}")
    public R<StoreProdSkuResVO> getSkuList(@PathVariable("storeProdId") Long storeProdId) {
        return R.ok(BeanUtil.toBean(storeProdService.getSkuList(storeProdId), StoreProdSkuResVO.class));
    }

    /**
     * 新增档口商品
     */
    @Log(title = "档口商品", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增档口商品", httpMethod = "POST", response = R.class)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody StoreProdVO storeProdVO) throws IOException {
        return R.ok(storeProdService.insertStoreProduct(BeanUtil.toBean(storeProdVO, StoreProdDTO.class)));
    }

    /**
     * 修改档口商品
     */
//    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @ApiOperation(value = "修改档口商品", httpMethod = "PUT", response = R.class)
    @Log(title = "档口商品", businessType = BusinessType.UPDATE)
    @PutMapping("/{storeProdId}")
    public R<Integer> edit(@PathVariable Long storeProdId, @Validated @RequestBody StoreProdVO storeProdVO) throws IOException {
        return R.ok(storeProdService.updateStoreProduct(storeProdId, BeanUtil.toBean(storeProdVO, StoreProdDTO.class)));
    }

    /**
     * 修改档口商品状态
     */
//    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @Log(title = "修改档口商品状态", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改档口商品状态", httpMethod = "PUT", response = R.class)
    @PutMapping("/prod-status")
    public R editProdStatus(@Validated @RequestBody StoreProdStatusVO prodStatusVO) throws IOException {
        storeProdService.updateStoreProductStatus(BeanUtil.toBean(prodStatusVO, StoreProdStatusDTO.class));
        return R.ok();
    }

    /**
     * 获取档口图片空间
     */
//    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @ApiOperation(value = "获取档口图片空间", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/pic-space/{storeId}")
    public R<StoreProdPicSpaceResVO> getStoreProductPicSpace(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeProdService.getStoreProductPicSpace(storeId), StoreProdPicSpaceResVO.class));
    }


    /**
     * 导出档口商品列表
     */
//    @PreAuthorize("@ss.hasPermi('system:product:export')")
    @Log(title = "档口商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProduct storeProduct) {
        List<StoreProduct> list = storeProdService.selectStoreProductList(storeProduct);
        ExcelUtil<StoreProduct> util = new ExcelUtil<StoreProduct>(StoreProduct.class);
        util.exportExcel(response, list, "档口商品数据");
    }

    /**
     * 修改档口商品  不要掉不要掉
     */
//    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @ApiOperation(value = "修改档口商品", httpMethod = "PUT", response = R.class)
    @Log(title = "档口商品", businessType = BusinessType.UPDATE)
    @PutMapping("/update/{storeProdId}")
    public R<Integer> update111(@PathVariable Long storeProdId) throws IOException {
        return R.ok(storeProdService.update111(storeProdId));
    }

}
