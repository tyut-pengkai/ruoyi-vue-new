package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.xkt.service.IStoreProductStorageDemandDeducteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 档口商品入库抵扣需求Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/storages-demand-deductes")
public class StoreProductStorageDemandDeducteController extends XktBaseController {
    @Autowired
    private IStoreProductStorageDemandDeducteService storeProductStorageDemandDeducteService;

}
