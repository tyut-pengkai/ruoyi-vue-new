package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProdStorage.StoreProdStoragePageVO;
import com.ruoyi.web.controller.xkt.vo.storeProdStorage.StoreProdStorageResVO;
import com.ruoyi.web.controller.xkt.vo.storeProdStorage.StoreProdStorageVO;
import com.ruoyi.web.controller.xkt.vo.storeSale.StoreSaleExportVO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStorageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageResDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSaleDownloadDTO;
import com.ruoyi.xkt.dto.storeSale.StoreSaleExportDTO;
import com.ruoyi.xkt.service.IStoreProductStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品入库Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口商品入库")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/prod-storages")
public class StoreProductStorageController extends XktBaseController {

    final IStoreProductStorageService storeProdStorageService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "新增档口商品入库", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口商品入库", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody StoreProdStorageVO storeProdStorageVO) {
        return R.ok(storeProdStorageService.create(BeanUtil.toBean(storeProdStorageVO, StoreProdStorageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "查询档口商品入库列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreProdStoragePageResDTO>> page(@Validated @RequestBody StoreProdStoragePageVO pageVO) {
        return R.ok(storeProdStorageService.page(BeanUtil.toBean(pageVO, StoreProdStoragePageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "获取档口商品入库详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeProdStorageId}")
    public R<StoreProdStorageResVO> getInfo(@PathVariable("storeProdStorageId") Long storeProdStorageId) {
        return R.ok(BeanUtil.toBean(storeProdStorageService.selectByStoreProdStorageId(storeProdStorageId), StoreProdStorageResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "撤销档口商品入库", httpMethod = "DELETE", response = R.class)
    @Log(title = "撤销档口商品入库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdStorageId}")
    public R<Integer> remove(@PathVariable Long storeProdStorageId) {
        return R.ok(storeProdStorageService.deleteByStoreProdStorageId(storeProdStorageId));
    }


}
