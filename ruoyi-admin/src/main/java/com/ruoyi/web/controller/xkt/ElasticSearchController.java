package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.elasticSearch.EsProdBatchCreateVO;
import com.ruoyi.web.controller.xkt.vo.elasticSearch.EsProdBatchDeleteVO;
import com.ruoyi.xkt.dto.elasticSearch.EsProdBatchCreateDTO;
import com.ruoyi.xkt.dto.elasticSearch.EsProdBatchDeleteDTO;
import com.ruoyi.xkt.service.IElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * ES相关处理
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/es")
public class ElasticSearchController extends XktBaseController {

    final IElasticSearchService esService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "ES批量新增数据", businessType = BusinessType.INSERT)
    @PutMapping("/batch-create")
    public R<Integer> batchCreate(@RequestParam(required = false) Long storeId) {
        esService.batchCreate(storeId);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "ES批量新增数据（特定商品）", businessType = BusinessType.INSERT)
    @PutMapping("/prod/batch-create")
    public R<Integer> batchCreateProd(@Validated @RequestBody EsProdBatchCreateVO createVO) {
        esService.batchCreateProd(BeanUtil.toBean(createVO, EsProdBatchCreateDTO.class));
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "ES批量删除数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch-delete")
    public R<Integer> batchDelete(@RequestParam(required = false) Long storeId) {
        esService.batchDelete(storeId);
        return R.ok();
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "ES批量删除数据（特定商品）", businessType = BusinessType.DELETE)
    @DeleteMapping("/prod/batch-delete")
    public R<Integer> batchDeleteProd(@Validated @RequestBody EsProdBatchDeleteVO deleteVO) {
        esService.batchDeleteProd(BeanUtil.toBean(deleteVO, EsProdBatchDeleteDTO.class));
        return R.ok();
    }

}
