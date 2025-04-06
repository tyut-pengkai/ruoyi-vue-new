package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.store.StoreUpdateVO;
import com.ruoyi.xkt.dto.store.StoreCreateDTO;
import com.ruoyi.xkt.dto.store.StoreUpdateDTO;
import com.ruoyi.xkt.service.IStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 档口Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/stores")
public class StoreController extends XktBaseController {

    final IStoreService storeService;

    /**
     * 新增档口
     */
    @PreAuthorize("@ss.hasPermi('system:store:edit')")
    @Log(title = "新增档口", businessType = BusinessType.UPDATE)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody StoreCreateDTO createDTO) {
        return R.ok(storeService.create(createDTO));
    }

    /**
     * 修改档口基本信息
     */
    @PreAuthorize("@ss.hasPermi('system:store:edit')")
    @ApiOperation(value = "修改档口基本信息", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreUpdateVO storeUpdateVO) {
        return R.ok(storeService.updateStore(BeanUtil.toBean(storeUpdateVO, StoreUpdateDTO.class)));
    }


}
