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
import com.ruoyi.xkt.dto.storeProduct.*;
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

    @ApiOperation(value = "查询档口商品所有的风格", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/styles")
    public R<List<String>> getStyleList() {
        return R.ok(storeProdService.getStyleList());
    }

    @ApiOperation(value = "模糊查询档口商品", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/fuzzy/color")
    public R<List<StoreProdFuzzyColorResVO>> fuzzyQueryColorList(@RequestParam(value = "prodArtNum", required = false) String prodArtNum,
                                                                 @RequestParam("storeId") Long storeId) {
        return R.ok(BeanUtil.copyToList(storeProdService.fuzzyQueryColorList(storeId, prodArtNum), StoreProdFuzzyColorResVO.class));
    }

    @ApiOperation(value = "模糊查询档口商品(返回商品主图)", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/fuzzy/pic")
    public R<List<StoreProdFuzzyResPicVO>> fuzzyQueryResPicList(@RequestParam(value = "prodArtNum", required = false) String prodArtNum,
                                                                @RequestParam("storeId") Long storeId) {
        return R.ok(BeanUtil.copyToList(storeProdService.fuzzyQueryResPicList(storeId, prodArtNum), StoreProdFuzzyResPicVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "推广营销（新品馆）模糊查询最新30天上新商品", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/fuzzy/latest30")
    public R<List<StoreProdFuzzyLatest30ResVO>> fuzzyQueryLatest30List(@RequestParam(value = "prodArtNum", required = false) String prodArtNum,
                                                                       @RequestParam("storeId") Long storeId) {
        return R.ok(BeanUtil.copyToList(storeProdService.fuzzyQueryLatest30List(storeId, prodArtNum), StoreProdFuzzyLatest30ResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "查询档口商品列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreProdPageResDTO>> page(@Validated @RequestBody StoreProdPageVO pageVO) {
        return R.ok(storeProdService.page(BeanUtil.toBean(pageVO, StoreProdPageDTO.class)));
    }

    @ApiOperation(value = "获取档口商品详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/detail/{storeProdId}")
    public R<StoreProdResVO> getInfo(@PathVariable("storeProdId") Long storeProdId) {
        return R.ok(BeanUtil.toBean(storeProdService.selectStoreProductByStoreProdId(storeProdId), StoreProdResVO.class));
    }


    @ApiOperation(value = "PC获取档口商品详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/pc/detail/{storeProdId}")
    public R<StoreProdPCResVO> getPCInfo(@PathVariable("storeProdId") Long storeProdId) {
        return R.ok(BeanUtil.toBean(storeProdService.getPCInfo(storeProdId), StoreProdPCResVO.class));
    }

    @ApiOperation(value = "APP获取档口商品详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/app/detail/{storeProdId}")
    public R<StoreProdAppResVO> getAppInfo(@PathVariable("storeProdId") Long storeProdId) {
        return R.ok(BeanUtil.toBean(storeProdService.getAppInfo(storeProdId), StoreProdAppResVO.class));
    }

    @ApiOperation(value = "获取档口商品颜色及sku等", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/sku/{storeProdId}")
    public R<StoreProdSkuResVO> getSkuList(@PathVariable("storeProdId") Long storeProdId) {
        return R.ok(BeanUtil.toBean(storeProdService.getSkuList(storeProdId), StoreProdSkuResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "新增档口商品", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增档口商品", httpMethod = "POST", response = R.class)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody StoreProdVO storeProdVO) throws IOException {
        return R.ok(storeProdService.insertStoreProduct(BeanUtil.toBean(storeProdVO, StoreProdDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "修改档口商品", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口商品", businessType = BusinessType.UPDATE)
    @PutMapping("/{storeProdId}")
    public R<Integer> edit(@PathVariable Long storeProdId, @Validated @RequestBody StoreProdVO storeProdVO) throws IOException {
        return R.ok(storeProdService.updateStoreProduct(storeProdId, BeanUtil.toBean(storeProdVO, StoreProdDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "修改档口商品状态", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改档口商品状态", httpMethod = "PUT", response = R.class)
    @PutMapping("/prod-status")
    public R<Integer> editProdStatus(@Validated @RequestBody StoreProdStatusVO prodStatusVO) throws IOException {
        return R.ok(storeProdService.updateStoreProductStatus(BeanUtil.toBean(prodStatusVO, StoreProdStatusDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "删除商品", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除商品", httpMethod = "DELETE", response = R.class)
    @DeleteMapping()
    public R<Integer> batchDelete(@Validated @RequestBody StoreProdDeleteVO deleteVO) throws IOException {
        return R.ok(storeProdService.batchDelete(BeanUtil.toBean(deleteVO, StoreProdDeleteDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "获取档口图片空间", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/pic-space")
    public R<StoreProdPicSpaceResVO> getStoreProductPicSpace(@Validated @RequestBody StoreProdPicSpaceVO picSpaceVO) {
        return R.ok(BeanUtil.toBean(storeProdService.getStoreProductPicSpace(BeanUtil.toBean(picSpaceVO, StoreProdPicSpaceDTO.class)), StoreProdPicSpaceResVO.class));
    }

    @ApiOperation(value = "获取商品各个状态数量", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/status/num/{storeId}")
    public R<StoreProdStatusCountResVO> getStatusNum(@PathVariable Long storeId) {
        return R.ok(BeanUtil.toBean(storeProdService.getStatusNum(storeId), StoreProdStatusCountResVO.class));
    }

    @ApiOperation(value = "获取商品各个状态下的分类数量", httpMethod = "POST", response = R.class)
    @PostMapping(value = "/status/cate/num/{storeId}")
    public R<List<StoreProdStatusCateCountResVO>> getStatusCateNum(@Validated @RequestBody StoreProdStatusCateNumVO cateCountVO) {
        return R.ok(BeanUtil.copyToList(storeProdService.getStatusCateNum(
                BeanUtil.toBean(cateCountVO, StoreProdStatusCateNumDTO.class)), StoreProdStatusCateCountResVO.class));
    }

    @Log(title = "档口商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProduct storeProduct) {
        List<StoreProduct> list = storeProdService.selectStoreProductList(storeProduct);
        ExcelUtil<StoreProduct> util = new ExcelUtil<>(StoreProduct.class);
        util.exportExcel(response, list, "档口商品数据");
    }


}
