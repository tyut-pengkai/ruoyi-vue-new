package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeProductDemandTemplate.StoreDemandTemplateResVO;
import com.ruoyi.web.controller.xkt.vo.storeProductDemandTemplate.StoreDemandTemplateUpdateVO;
import com.ruoyi.xkt.dto.storeProductDemandTemplate.StoreDemandTemplateUpdateDTO;
import com.ruoyi.xkt.service.IStoreProductDemandTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 档口生产需求模板 Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口生产需求下载模板")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-templates")
public class StoreProductDemandTemplateController extends XktBaseController {

    final IStoreProductDemandTemplateService templateService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "查询档口设置的需求下载模板", httpMethod = "GET", response = R.class)
    @GetMapping("/{storeId}")
    public R<StoreDemandTemplateResVO> getTemplate(@PathVariable Long storeId) {
        return R.ok(BeanUtil.toBean(templateService.getTemplate(storeId), StoreDemandTemplateResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "更新档口需求下载模板", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "更新档口需求下载模板", httpMethod = "PUT", response = R.class)
    @PutMapping("")
    public R<Integer> updateTemplate(@RequestBody StoreDemandTemplateUpdateVO updateVO) {
        return R.ok(templateService.updateTemplate(BeanUtil.toBean(updateVO, StoreDemandTemplateUpdateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @Log(title = "重置档口需求模板", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "重置档口需求模板", httpMethod = "PUT", response = R.class)
    @PutMapping("/reset/{storeId}")
    public R<Integer> resetTemplate(@PathVariable Long storeId) {
        return R.ok(templateService.initTemplate(storeId));
    }


}
