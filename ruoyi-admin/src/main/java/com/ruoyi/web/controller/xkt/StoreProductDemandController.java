package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeCustomer.StoreCusPageVO;
import com.ruoyi.web.controller.xkt.vo.storeProductDemand.StoreProdDemandPageResVO;
import com.ruoyi.web.controller.xkt.vo.storeProductDemand.StoreProdDemandPageVO;
import com.ruoyi.web.controller.xkt.vo.storeProductDemand.StoreProdDemandQuantityVO;
import com.ruoyi.web.controller.xkt.vo.storeProductDemand.StoreProdDemandVO;
import com.ruoyi.xkt.domain.StoreProductDemand;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandPageDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandPageResDTO;
import com.ruoyi.xkt.dto.storeProductDemand.StoreProdDemandQuantityDTO;
import com.ruoyi.xkt.service.IStoreProductDemandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品需求单Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口商品需求单")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/prod-demands")
public class StoreProductDemandController extends XktBaseController {

    final IStoreProductDemandService storeProdDemandService;


    // TODO 入库单判断需求单是否存在
    // TODO 入库单判断需求单是否存在
    // TODO 入库单判断需求单是否存在
    // TODO 入库单判断需求单是否存在

    /**
     * 根据货号获取所有颜色的库存数量、在产数量
     */
    @ApiOperation(value = "根据货号获取所有颜色的库存数量、在产数量", httpMethod = "GET", response = R.class)
    @PreAuthorize("@ss.hasPermi('system:demand:query')")
    @GetMapping(value = "/exists-quantity/{storeId}/{storeProdId}")
    public R<List<StoreProdDemandQuantityVO>> getStockAndProduceQuantity(@PathVariable("storeId") Long storeId, @PathVariable("storeProdId") Long storeProdId) {
        return  R.ok(BeanUtil.copyToList(storeProdDemandService.getStockAndProduceQuantity(storeId, storeProdId), StoreProdDemandQuantityVO.class));
    }

    /**
     * 新增档口商品需求单
     */
    @PreAuthorize("@ss.hasPermi('system:demand:add')")
    @ApiOperation(value = "新增档口商品需求单", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口商品需求单", businessType = BusinessType.INSERT)
    @PostMapping("")
    public R<Integer> add(@Validated @RequestBody StoreProdDemandVO prodDemandVO) {
        return R.ok(storeProdDemandService.createDemand(BeanUtil.toBean(prodDemandVO, StoreProdDemandDTO.class)));
    }

    /**
     * 查询档口商品需求单列表
     */
    @PreAuthorize("@ss.hasPermi('system:customer:list')")
    @ApiOperation(value = "查询档口商品需求单列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreProdDemandPageResDTO>> selectPage(@Validated @RequestBody StoreProdDemandPageVO pageVO) {
        return R.ok(storeProdDemandService.selectPage(BeanUtil.toBean(pageVO, StoreProdDemandPageDTO.class)));
    }


    /**
     * 获取档口商品需求单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:demand:query')")
    @GetMapping(value = "/{storeProdDemandId}")
    public R getInfo(@PathVariable("storeProdDemandId") Long storeProdDemandId) {
        return success(storeProdDemandService.selectStoreProductDemandByStoreProdDemandId(storeProdDemandId));
    }


    /**
     * 修改档口商品需求单
     */
    @PreAuthorize("@ss.hasPermi('system:demand:edit')")
    @Log(title = "档口商品需求单", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductDemand storeProductDemand) {
        return success(storeProdDemandService.updateStoreProductDemand(storeProductDemand));
    }

    /**
     * 删除档口商品需求单
     */
    @PreAuthorize("@ss.hasPermi('system:demand:remove')")
    @Log(title = "档口商品需求单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdDemandIds}")
    public R remove(@PathVariable Long[] storeProdDemandIds) {
        return success(storeProdDemandService.deleteStoreProductDemandByStoreProdDemandIds(storeProdDemandIds));
    }
}
