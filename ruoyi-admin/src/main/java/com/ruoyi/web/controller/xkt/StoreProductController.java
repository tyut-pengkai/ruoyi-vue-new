package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.BeansUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProd.*;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.dto.storeProduct.*;
import com.ruoyi.xkt.service.IStoreProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prods")
@Api(tags = "档口商品")
public class StoreProductController extends XktBaseController {
    @Autowired
    private IStoreProductService storeProductService;

    /**
     * 查询档口商品列表
     */
    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProdPageVO pageVO) {
        startPage();
        List<StoreProdPageResDTO> list = storeProductService.selectPage(ObjectUtils.isEmpty(pageVO) ? null : BeansUtils.convertObject(pageVO, StoreProdPageDTO.class));
        // TODO 处理返回的VO
        // TODO 处理返回的VO
        // TODO 处理返回的VO
        return getDataTable(list);
    }

    /**
     * 导出档口商品列表
     */
    @PreAuthorize("@ss.hasPermi('system:product:export')")
    @Log(title = "档口商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProduct storeProduct) {
        List<StoreProduct> list = storeProductService.selectStoreProductList(storeProduct);
        ExcelUtil<StoreProduct> util = new ExcelUtil<StoreProduct>(StoreProduct.class);
        util.exportExcel(response, list, "档口商品数据");
    }

    /**
     * 获取档口商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @GetMapping(value = "/{storeProdId}")
    public R getInfo(@PathVariable("storeProdId") Long storeProdId) {
        return success(BeansUtils.convertObject(storeProductService.selectStoreProductByStoreProdId(storeProdId), StoreProdResVO.class));
    }

    /**
     * 新增档口商品
     */
//    @PreAuthorize("@ss.hasPermi('system:product:add')")
    @Log(title = "档口商品", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增档口商品", httpMethod = "POST", response = R.class)
    @PostMapping
    public R add(@Validated  @RequestBody StoreProdVO storeProdVO) {
        return success(storeProductService.insertStoreProduct(BeanUtil.toBean(storeProdVO, StoreProdDTO.class)));
    }

    /**
     * 修改档口商品
     */
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @ApiOperation(value = "修改档口商品", httpMethod = "POST", response = R.class)
    @Log(title = "档口商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@Validated  @RequestBody StoreProdVO storeProdVO) {
        return success(storeProductService.updateStoreProduct(BeansUtils.convertObject(storeProdVO, StoreProdDTO.class)));
    }

    /**
     * 修改档口商品状态
     */
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @Log(title = "档口商品", businessType = BusinessType.UPDATE)
    @PutMapping("/prod-status")
    public R editProdStatus(@Validated  @RequestBody StoreProdStatusVO prodStatusVO) {
        storeProductService.updateStoreProductStatus(BeansUtils.convertObject(prodStatusVO, StoreProdStatusDTO.class));
        return success();
    }



}
