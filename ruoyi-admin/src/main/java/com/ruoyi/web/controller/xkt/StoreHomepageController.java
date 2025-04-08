package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeHomepage.StoreHomeVO;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeDTO;
import com.ruoyi.xkt.service.IStoreHomepageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 档口首页装修Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口首页装修")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-homes")
public class StoreHomepageController extends XktBaseController {

    final IStoreHomepageService storeHomeService;

    /**
     * 新增档口首页装修数据
     */
    @PreAuthorize("@ss.hasPermi('system:homepage:add')")
    @ApiOperation(value = "新增档口首页装修数据", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口首页装修数据", businessType = BusinessType.INSERT)
    @PostMapping("/{storeId}/{templateNum}")
    public R<Integer> add(@PathVariable("storeId") Long storeId, @PathVariable("templateNum") Integer templateNum,
                          @Validated @RequestBody StoreHomeVO homepageVO) {
        return R.ok(storeHomeService.insert(storeId, templateNum, BeanUtil.toBean(homepageVO, StoreHomeDTO.class)));
    }

    /**
     * 查询档口首页装修数据
     */
    @PreAuthorize("@ss.hasPermi('system:sale:query')")
    @ApiOperation(value = "查询档口首页装修数据", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}")
    public R<StoreHomeVO> getInfo(@PathVariable("storeId") Long storeId) {
        return R.ok(BeanUtil.toBean(storeHomeService.selectByStoreId(storeId), StoreHomeVO.class));
    }

    /**
     * 修改档口首页装修数据
     */
    @PreAuthorize("@ss.hasPermi('system:homepage:edit')")
    @ApiOperation(value = "修改档口首页装修数据", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口首页装修数据", businessType = BusinessType.UPDATE)
    @PutMapping("/{storeId}/{templateNum}")
    public R<Integer> edit(@PathVariable("storeId") Long storeId, @PathVariable("templateNum") Integer templateNum,
                           @Validated @RequestBody StoreHomeVO homepageVO) {
        return R.ok(storeHomeService.updateStoreHomepage(storeId, templateNum, BeanUtil.toBean(homepageVO, StoreHomeDTO.class)));
    }

}
