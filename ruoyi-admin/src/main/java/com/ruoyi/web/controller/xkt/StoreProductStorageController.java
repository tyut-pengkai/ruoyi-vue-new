package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProd.StoreProdPageVO;
import com.ruoyi.web.controller.xkt.vo.storeProdStorage.StoreProdStoragePageVO;
import com.ruoyi.web.controller.xkt.vo.storeProdStorage.StoreProdStorageResVO;
import com.ruoyi.web.controller.xkt.vo.storeProdStorage.StoreProdStorageVO;
import com.ruoyi.xkt.domain.StoreProductStorage;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStorageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStoragePageResDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreProdStorageResDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageDTO;
import com.ruoyi.xkt.service.IStoreProductStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做
    // TODO 需求抵扣明细没做

    /**
     * 新增档口商品入库
     */
    @PreAuthorize("@ss.hasPermi('system:storage:add')")
    @ApiOperation(value = "新增档口商品入库", httpMethod = "POST", response = R.class)
    @Log(title = "新增档口商品入库", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@Validated @RequestBody StoreProdStorageVO storeProdStorageVO) {
        return R.ok(storeProdStorageService.create(BeanUtil.toBean(storeProdStorageVO, StoreProdStorageDTO.class)));
    }

    /**
     * 查询档口商品入库列表
     */
    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @ApiOperation(value = "查询档口商品入库列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<StoreProdStoragePageResDTO>> page(@Validated @RequestBody StoreProdStoragePageVO pageVO) {
        return R.ok(storeProdStorageService.page(BeanUtil.toBean(pageVO, StoreProdStoragePageDTO.class)));
    }

    /**
     * 获取档口商品入库详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:storage:query')")
    @ApiOperation(value = "获取档口商品入库详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{storeProdStorId}")
    public R<StoreProdStorageResVO> getInfo(@PathVariable("storeProdStorId") Long storeProdStorId) {
        return R.ok(BeanUtil.toBean(storeProdStorageService.selectByStoreProdStorId(storeProdStorId), StoreProdStorageResVO.class));
    }

    /**
     * 撤销档口商品入库
     */
    @PreAuthorize("@ss.hasPermi('system:storage:remove')")
    @ApiOperation(value = "撤销档口商品入库", httpMethod = "DELETE", response = R.class)
    @Log(title = "撤销档口商品入库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdStorId}")
    public R remove(@PathVariable Long storeProdStorId) {
        return R.ok(storeProdStorageService.deleteByStoreProdStorId(storeProdStorId));
    }






    /**
     * 查询档口商品入库列表
     */
    @PreAuthorize("@ss.hasPermi('system:storage:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductStorage storeProductStorage) {
        startPage();
        List<StoreProductStorage> list = storeProdStorageService.selectStoreProductStorageList(storeProductStorage);
        return getDataTable(list);
    }

    /**
     * 导出档口商品入库列表
     */
    @PreAuthorize("@ss.hasPermi('system:storage:export')")
    @Log(title = "档口商品入库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductStorage storeProductStorage) {
        List<StoreProductStorage> list = storeProdStorageService.selectStoreProductStorageList(storeProductStorage);
        ExcelUtil<StoreProductStorage> util = new ExcelUtil<StoreProductStorage>(StoreProductStorage.class);
        util.exportExcel(response, list, "档口商品入库数据");
    }





    /**
     * 修改档口商品入库
     */
    @PreAuthorize("@ss.hasPermi('system:storage:edit')")
    @Log(title = "档口商品入库", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductStorage storeProductStorage) {
        return success(storeProdStorageService.updateStoreProductStorage(storeProductStorage));
    }



}
