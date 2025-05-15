package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.storeProductDemand.StoreProdDemandQuantityVO;
import com.ruoyi.web.controller.xkt.vo.storeProductStorageDemandDeduct.StoreProdStorageDemandDeductVO;
import com.ruoyi.xkt.service.IStoreProductStorageDemandDeducteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 档口商品入库抵扣需求Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口商品入库抵扣需求")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/storages-demand-deducts")
public class StoreProductStorageDemandDeductController extends XktBaseController {

    final IStoreProductStorageDemandDeducteService prodStorageDemandDeductService;

    /**
     * 入库单列表获取抵扣需求明细
     */
    @ApiOperation(value = "入库单列表获取抵扣需求明细", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}/{storageCode}")
    public R<StoreProdStorageDemandDeductVO> getStorageDemandDeductList(@PathVariable ("storeId") Long storeId, @PathVariable("storageCode") String storageCode) {
        return R.ok(BeanUtil.toBean(prodStorageDemandDeductService.getStorageDemandDeductList(storeId, storageCode), StoreProdStorageDemandDeductVO.class));
    }

}
