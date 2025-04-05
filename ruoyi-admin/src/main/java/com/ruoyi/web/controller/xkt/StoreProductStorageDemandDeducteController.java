package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductStorageDemandDeduct;
import com.ruoyi.xkt.service.IStoreProductStorageDemandDeducteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
