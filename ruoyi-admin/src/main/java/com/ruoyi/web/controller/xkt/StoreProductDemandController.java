package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProductDemand.*;
import com.ruoyi.xkt.dto.storeProductDemand.*;
import com.ruoyi.xkt.dto.storeProductDemandTemplate.StoreDemandTemplateResDTO;
import com.ruoyi.xkt.service.IStoreProductDemandService;
import com.ruoyi.xkt.service.IStoreProductDemandTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    final IStoreProductDemandTemplateService storeTemplateService;

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
        // 获取哪些列需要隐藏
        StoreDemandTemplateResDTO template = this.storeTemplateService.getTemplate(exportVO.getStoreId());
        if (ObjectUtils.isNotEmpty(template)) {
            List<String> hideColumns = this.getHideColumns(template);
            if (CollectionUtils.isNotEmpty(hideColumns)) {
                util.hideColumn(hideColumns.toArray(new String[0]));
            }
        }
        // 设置下载excel名
        String encodedFileName = URLEncoder.encode("生产需求" + DateUtils.getDate(), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName + ".xlsx");
        util.exportExcel(response, downloadList, "生产需求记录");
    }

    /**
     * 动态设置下载的excel列
     *
     * @param template 档口下载的需求模板
     * @return 隐藏的列
     */
    private List<String> getHideColumns(StoreDemandTemplateResDTO template) {
        List<String> hideColumns = new ArrayList<>();
        if (Objects.equals(template.getSelectSize30(), Constants.UNSELECTED)) {
            hideColumns.add("size30Quantity");
        }
        if (Objects.equals(template.getSelectSize31(), Constants.UNSELECTED)) {
            hideColumns.add("size31Quantity");
        }
        if (Objects.equals(template.getSelectSize32(), Constants.UNSELECTED)) {
            hideColumns.add("size32Quantity");
        }
        if (Objects.equals(template.getSelectSize33(), Constants.UNSELECTED)) {
            hideColumns.add("size33Quantity");
        }
        if (Objects.equals(template.getSelectSize34(), Constants.UNSELECTED)) {
            hideColumns.add("size34Quantity");
        }
        if (Objects.equals(template.getSelectSize35(), Constants.UNSELECTED)) {
            hideColumns.add("size35Quantity");
        }
        if (Objects.equals(template.getSelectSize36(), Constants.UNSELECTED)) {
            hideColumns.add("size36Quantity");
        }
        if (Objects.equals(template.getSelectSize37(), Constants.UNSELECTED)) {
            hideColumns.add("size37Quantity");
        }
        if (Objects.equals(template.getSelectSize38(), Constants.UNSELECTED)) {
            hideColumns.add("size38Quantity");
        }
        if (Objects.equals(template.getSelectSize39(), Constants.UNSELECTED)) {
            hideColumns.add("size39Quantity");
        }
        if (Objects.equals(template.getSelectSize40(), Constants.UNSELECTED)) {
            hideColumns.add("size40Quantity");
        }
        if (Objects.equals(template.getSelectSize41(), Constants.UNSELECTED)) {
            hideColumns.add("size41Quantity");
        }
        if (Objects.equals(template.getSelectSize42(), Constants.UNSELECTED)) {
            hideColumns.add("size42Quantity");
        }
        if (Objects.equals(template.getSelectSize43(), Constants.UNSELECTED)) {
            hideColumns.add("size43Quantity");
        }
        if (Objects.equals(template.getSelectFacName(), Constants.UNSELECTED)) {
            hideColumns.add("facName");
        }
        if (Objects.equals(template.getSelectDemandCode(), Constants.UNSELECTED)) {
            hideColumns.add("code");
        }
        if (Objects.equals(template.getSelectMakeTime(), Constants.UNSELECTED)) {
            hideColumns.add("createTime");
        }
        if (Objects.equals(template.getSelectFactoryArtNum(), Constants.UNSELECTED)) {
            hideColumns.add("factoryArtNum");
        }
        if (Objects.equals(template.getSelectProdArtNum(), Constants.UNSELECTED)) {
            hideColumns.add("prodArtNum");
        }
        if (Objects.equals(template.getSelectColorName(), Constants.UNSELECTED)) {
            hideColumns.add("colorName");
        }
        if (Objects.equals(template.getSelectShoeUpperLiningMaterial(), Constants.UNSELECTED)) {
            hideColumns.add("shoeUpperLiningMaterial");
        }
        if (Objects.equals(template.getSelectShaftMaterial(), Constants.UNSELECTED)) {
            hideColumns.add("shaftMaterial");
        }
        if (Objects.equals(template.getSelectDemandStatus(), Constants.UNSELECTED)) {
            hideColumns.add("demandStatus");
        }
        if (Objects.equals(template.getSelectEmergency(), Constants.UNSELECTED)) {
            hideColumns.add("emergency");
        }
        if (Objects.equals(template.getSelectPartnerName(), Constants.UNSELECTED)) {
            hideColumns.add("partnerName");
        }
        if (Objects.equals(template.getSelectTrademark(), Constants.UNSELECTED)) {
            hideColumns.add("trademark");
        }
        if (Objects.equals(template.getSelectShoeType(), Constants.UNSELECTED)) {
            hideColumns.add("shoeType");
        }
        if (Objects.equals(template.getSelectShoeSize(), Constants.UNSELECTED)) {
            hideColumns.add("shoeSize");
        }
        if (Objects.equals(template.getSelectMainSkin(), Constants.UNSELECTED)) {
            hideColumns.add("mainSkin");
        }
        if (Objects.equals(template.getSelectMainSkinUsage(), Constants.UNSELECTED)) {
            hideColumns.add("mainSkinUsage");
        }
        if (Objects.equals(template.getSelectMatchSkin(), Constants.UNSELECTED)) {
            hideColumns.add("matchSkin");
        }
        if (Objects.equals(template.getSelectMatchSkinUsage(), Constants.UNSELECTED)) {
            hideColumns.add("matchSkinUsage");
        }
        if (Objects.equals(template.getSelectNeckline(), Constants.UNSELECTED)) {
            hideColumns.add("neckline");
        }
        if (Objects.equals(template.getSelectInsole(), Constants.UNSELECTED)) {
            hideColumns.add("insole");
        }
        if (Objects.equals(template.getSelectFastener(), Constants.UNSELECTED)) {
            hideColumns.add("fastener");
        }
        if (Objects.equals(template.getSelectShoeAccessories(), Constants.UNSELECTED)) {
            hideColumns.add("shoeAccessories");
        }
        if (Objects.equals(template.getSelectToeCap(), Constants.UNSELECTED)) {
            hideColumns.add("toeCap");
        }
        if (Objects.equals(template.getSelectEdgeBinding(), Constants.UNSELECTED)) {
            hideColumns.add("edgeBinding");
        }
        if (Objects.equals(template.getSelectMidOutsole(), Constants.UNSELECTED)) {
            hideColumns.add("midOutsole");
        }
        if (Objects.equals(template.getSelectPlatformSole(), Constants.UNSELECTED)) {
            hideColumns.add("platformSole");
        }
        if (Objects.equals(template.getSelectMidsoleFactoryCode(), Constants.UNSELECTED)) {
            hideColumns.add("midsoleFactoryCode");
        }
        if (Objects.equals(template.getSelectOutsoleFactoryCode(), Constants.UNSELECTED)) {
            hideColumns.add("outsoleFactoryCode");
        }
        if (Objects.equals(template.getSelectHeelFactoryCode(), Constants.UNSELECTED)) {
            hideColumns.add("heelFactoryCode");
        }
        if (Objects.equals(template.getSelectComponents(), Constants.UNSELECTED)) {
            hideColumns.add("components");
        }
        if (Objects.equals(template.getSelectSecondSoleMaterial(), Constants.UNSELECTED)) {
            hideColumns.add("secondSoleMaterial");
        }
        if (Objects.equals(template.getSelectSecondUpperMaterial(), Constants.UNSELECTED)) {
            hideColumns.add("secondUpperMaterial");
        }
        if (Objects.equals(template.getSelectQuantity(), Constants.UNSELECTED)) {
            hideColumns.add("quantity");
        }
        return hideColumns;
    }


}
