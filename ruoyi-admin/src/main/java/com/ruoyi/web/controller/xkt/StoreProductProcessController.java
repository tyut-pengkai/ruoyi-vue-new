package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeProdProcess.StoreProdProcessResVO;
import com.ruoyi.web.controller.xkt.vo.storeProdProcess.StoreProdProcessUpdateVO;
import com.ruoyi.xkt.dto.storeProdProcess.StoreProdProcessUpdateDTO;
import com.ruoyi.xkt.service.IStoreProductProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 档口商品工艺信息Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-processes")
@RequiredArgsConstructor
@Api(tags = "档口商品工艺")
public class StoreProductProcessController extends XktBaseController {

    final IStoreProductProcessService prodProcessService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "获取档口商品工艺详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeProdId}")
    public R<StoreProdProcessResVO> getProcess(@PathVariable("storeProdId") Long storeProdId) {
        return R.ok(BeanUtil.toBean(prodProcessService.getProcess(storeProdId), StoreProdProcessResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "更新商品工艺信息", httpMethod = "PUT", response = R.class)
    @Log(title = "更新商品工艺信息", businessType = BusinessType.UPDATE)
    @PutMapping("/{storeProdId}")
    public R<Integer> update(@PathVariable Long storeProdId, @Validated @RequestBody StoreProdProcessUpdateVO processUpdateVO) throws IOException {
        return R.ok(prodProcessService.update(storeProdId, BeanUtil.toBean(processUpdateVO, StoreProdProcessUpdateDTO.class)));
    }


}
