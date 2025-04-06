package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.store.StoreUpdateVO;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.dto.store.StoreCreateDTO;
import com.ruoyi.xkt.dto.store.StoreUpdateDTO;
import com.ruoyi.xkt.service.IStoreService;
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














    /**
     * 查询档口列表
     */
    @PreAuthorize("@ss.hasPermi('system:store:list')")
    @GetMapping("/list")
    public TableDataInfo list(Store store) {
        startPage();
        List<Store> list = storeService.selectStoreList(store);
        return getDataTable(list);
    }

    /**
     * 导出档口列表
     */
    @PreAuthorize("@ss.hasPermi('system:store:export')")
    @Log(title = "档口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Store store) {
        List<Store> list = storeService.selectStoreList(store);
        ExcelUtil<Store> util = new ExcelUtil<Store>(Store.class);
        util.exportExcel(response, list, "档口数据");
    }

    /**
     * 获取档口详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:store:query')")
    @GetMapping(value = "/{storeId}")
    public R getInfo(@PathVariable("storeId") Long storeId) {
        return success(storeService.selectStoreByStoreId(storeId));
    }



    /**
     * 删除档口
     */
    @PreAuthorize("@ss.hasPermi('system:store:remove')")
    @Log(title = "档口", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeIds}")
    public R remove(@PathVariable Long[] storeIds) {
        return success(storeService.deleteStoreByStoreIds(storeIds));
    }
}
