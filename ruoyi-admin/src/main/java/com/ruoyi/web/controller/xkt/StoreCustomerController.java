package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.storeCustomer.StoreCusFuzzyResVO;
import com.ruoyi.web.controller.xkt.vo.storeCustomer.StoreCusPageVO;
import com.ruoyi.web.controller.xkt.vo.storeCustomer.StoreCusVO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageDTO;
import com.ruoyi.xkt.dto.storeCustomer.StoreCusPageResDTO;
import com.ruoyi.xkt.service.IStoreCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 档口客户Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "档口客户")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-customers")
public class StoreCustomerController extends XktBaseController {

    final IStoreCustomerService storeCusService;

    /**
     * 模糊查询档口客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:query')")
    @ApiOperation(value = "模糊查询档口客户", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/fuzzy")
    public R<List<StoreCusFuzzyResVO>> fuzzyQueryColorList(@RequestParam(value = "cusName", required = false) String cusName,
                                                           @RequestParam("storeId") Long storeId) {
        return R.ok(BeanUtil.copyToList(storeCusService.fuzzyQueryList(storeId, cusName), StoreCusFuzzyResVO.class));
    }


    /**
     * 新增档口客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:add')")
    @ApiOperation(value = "新增档口客户", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口客户", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody StoreCusVO storeCusVO) {
        return R.ok(storeCusService.create(BeanUtil.toBean(storeCusVO, StoreCusDTO.class)));
    }

    /**
     * 修改档口客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:edit')")
    @ApiOperation(value = "修改档口客户", httpMethod = "PUT", response = R.class)
    @Log(title = "修改档口客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody StoreCusVO storeCusVO) {
        return R.ok(storeCusService.updateStoreCus(BeanUtil.toBean(storeCusVO, StoreCusDTO.class)));
    }

    /**
     * 删除档口客户
     */
    @PreAuthorize("@ss.hasPermi('system:customer:remove')")
    @ApiOperation(value = "删除档口客户", httpMethod = "DELETE", response = R.class)
    @Log(title = "删除档口客户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeCusId}")
    public R remove(@PathVariable Long storeCusId) {
        storeCusService.deleteStoreCus(storeCusId);
        return success();
    }

    /**
     * 获取档口客户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:customer:query')")
    @ApiOperation(value = "获取档口客户详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeCusId}")
    public R<StoreCusVO> getInfo(@PathVariable("storeCusId") Long storeCusId) {
        return R.ok(BeanUtil.toBean(storeCusService.selectStoreCustomerByStoreCusId(storeCusId), StoreCusVO.class));
    }

    /**
     * 查询档口客户列表
     */
    @PreAuthorize("@ss.hasPermi('system:customer:list')")
    @ApiOperation(value = "查询档口客户列表", httpMethod = "GET", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreCusPageResDTO>> selectPage(@Validated @RequestBody StoreCusPageVO pageVO) {
        return R.ok(storeCusService.selectPage(BeanUtil.toBean(pageVO, StoreCusPageDTO.class)));
    }

}
