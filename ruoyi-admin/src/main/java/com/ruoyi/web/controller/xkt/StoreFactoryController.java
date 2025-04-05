package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeFactory.StoreFactoryPageVO;
import com.ruoyi.web.controller.xkt.vo.storeFactory.StoreFactoryVO;
import com.ruoyi.xkt.domain.StoreFactory;
import com.ruoyi.xkt.dto.storeFactory.StoreFactoryDTO;
import com.ruoyi.xkt.dto.storeFactory.StoreFactoryPageDTO;
import com.ruoyi.xkt.dto.storeFactory.StoreFactoryResDTO;
import com.ruoyi.xkt.service.IStoreFactoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口合作工厂Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口合作工厂")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-factories")
public class StoreFactoryController extends XktBaseController {

    final IStoreFactoryService storeFactoryService;

    /**
     * 新增档口合作工厂
     */
    @PreAuthorize("@ss.hasPermi('system:factory:add')")
    @ApiOperation(value = "新增档口合作工厂", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口合作工厂", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody StoreFactoryVO storeFactoryVO) {
        return R.ok(storeFactoryService.insertStoreFactory(BeanUtil.toBean(storeFactoryVO, StoreFactoryDTO.class)));
    }


    /**
     * 修改档口合作工厂
     */
    @PreAuthorize("@ss.hasPermi('system:factory:edit')")
    @ApiOperation(value = "修改档口合作工厂", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口合作工厂", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreFactoryVO storeFactoryVO) {
        return R.ok(storeFactoryService.updateStoreFactory(BeanUtil.toBean(storeFactoryVO, StoreFactoryDTO.class)));
    }

    /**
     * 查询档口合作工厂列表
     */
    @PreAuthorize("@ss.hasPermi('system:customer:list')")
    @ApiOperation(value = "查询档口合作工厂列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreFactoryResDTO>> selectPage(@Validated @RequestBody StoreFactoryPageVO pageVO) {
        return R.ok(storeFactoryService.selectFactoryPage(BeanUtil.toBean(pageVO, StoreFactoryPageDTO.class)));
    }

    /**
     * 获取档口合作工厂详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:factory:query')")
    @ApiOperation(value = "获取档口合作工厂详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeId}/{storeFactoryId}")
    public R<StoreFactoryVO> getInfo(@PathVariable("storeId") Long storeId, @PathVariable("storeFactoryId") Long storeFactoryId) {
        return R.ok(BeanUtil.toBean(storeFactoryService.selectByStoreFacId(storeId ,storeFactoryId), StoreFactoryVO.class));
    }



    /**
     * 导出档口合作工厂列表
     */
    @PreAuthorize("@ss.hasPermi('system:factory:export')")
    @Log(title = "档口合作工厂", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreFactory storeFactory) {
        List<StoreFactory> list = storeFactoryService.selectStoreFactoryList(storeFactory);
        ExcelUtil<StoreFactory> util = new ExcelUtil<StoreFactory>(StoreFactory.class);
        util.exportExcel(response, list, "档口合作工厂数据");
    }

}
