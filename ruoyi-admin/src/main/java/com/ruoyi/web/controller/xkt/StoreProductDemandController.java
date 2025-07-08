package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProductDemand.*;
import com.ruoyi.xkt.dto.storeProductDemand.*;
import com.ruoyi.xkt.service.IStoreProductDemandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    @ApiOperation(value = "获取所有需求单状态", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/status/num/{storeId}")
    public R<StoreProdDemandStatusCountResVO> getStatusNum(@PathVariable Long storeId) {
        return R.ok(BeanUtil.toBean(storeProdDemandService.getStatusNum(storeId), StoreProdDemandStatusCountResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "商品入库校验是否存在需求单", httpMethod = "POST", response = R.class)
    @Log(title = "商品入库校验是否存在需求单", businessType = BusinessType.INSERT)
    @PostMapping("/verify")
    public R<StoreProdDemandVerifyResVO> verifyDemandExist(@Validated @RequestBody StoreProdDemandVerifyVO demandVerifyVO) {
        return R.ok(BeanUtil.toBean(storeProdDemandService
                .verifyDemandExist(BeanUtil.toBean(demandVerifyVO, StoreProdDemandVerifyDTO.class)), StoreProdDemandVerifyResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "根据货号获取所有颜色的库存数量、在产数量", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/exists-quantity/{storeId}/{storeProdId}")
    public R<List<StoreProdDemandQuantityVO>> getStockAndProduceQuantity(@PathVariable("storeId") Long storeId, @PathVariable("storeProdId") Long storeProdId) {
        return R.ok(BeanUtil.copyToList(storeProdDemandService.getStockAndProduceQuantity(storeId, storeProdId), StoreProdDemandQuantityVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "新增档口商品需求单", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口商品需求单", businessType = BusinessType.INSERT)
    @PostMapping("")
    public R<Integer> add(@Validated @RequestBody StoreProdDemandVO prodDemandVO) {
        return R.ok(storeProdDemandService.createDemand(BeanUtil.toBean(prodDemandVO, StoreProdDemandDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "查询档口商品需求单列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreProdDemandPageResDTO>> selectPage(@Validated @RequestBody StoreProdDemandPageVO pageVO) {
        return R.ok(storeProdDemandService.selectPage(BeanUtil.toBean(pageVO, StoreProdDemandPageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "点击安排生产", httpMethod = "PUT", response = R.class)
    @Log(title = "点击安排生产", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> updateWorkingStatus(@Validated @RequestBody StoreProdDemandWorkingVO workingVO) {
        return R.ok(storeProdDemandService.updateWorkingStatus(BeanUtil.toBean(workingVO, StoreProdDemandWorkingDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "删除档口商品需求单", httpMethod = "DELETE", response = R.class)
    @Log(title = "删除档口商品需求单", businessType = BusinessType.DELETE)
    @DeleteMapping("")
    public R<Integer> remove(@Validated @RequestBody StoreProdDemandDeleteVO deleteVO) {
        return R.ok(storeProdDemandService.deleteByStoreProdDemandIds(BeanUtil.toBean(deleteVO, StoreProdDemandDeleteDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "全部完成", httpMethod = "PUT", response = R.class)
    @Log(title = "全部完成", businessType = BusinessType.UPDATE)
    @PutMapping("/finish-all")
    public R<Integer> finishAll(@Validated @RequestBody StoreProdDemandFinishAllVO allFinishVO) {
        return R.ok(storeProdDemandService.finishAll(BeanUtil.toBean(allFinishVO, StoreProdDemandFinishAllDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "导出生产需求", httpMethod = "POST", response = R.class)
    @Log(title = "导出生产需求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Validated @RequestBody StoreProdDemandExportVO exportVO) throws UnsupportedEncodingException {
        List<StoreProdDemandDownloadDTO> downloadList = storeProdDemandService.export(BeanUtil.toBean(exportVO, StoreProdDemandExportDTO.class));
        ExcelUtil<StoreProdDemandDownloadDTO> util = new ExcelUtil<>(StoreProdDemandDownloadDTO.class);
        // 设置下载excel名
        String encodedFileName = URLEncoder.encode("生产需求" + DateUtils.getDate(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        util.exportExcel(response, downloadList, "生产需求记录");
    }


}
