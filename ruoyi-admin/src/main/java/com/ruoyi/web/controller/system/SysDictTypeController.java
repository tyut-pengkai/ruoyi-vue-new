package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.vo.dictType.*;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.dto.dictType.DictTypeDTO;
import com.ruoyi.system.domain.dto.dictType.DictTypeDeleteDTO;
import com.ruoyi.system.domain.dto.dictType.DictTypePageDTO;
import com.ruoyi.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@Api(tags = "系统基础数据 - 字典类型")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/sys/dict/type")
public class SysDictTypeController extends BaseController {

    final ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @ApiOperation(value = "新增字典类型", httpMethod = "POST", response = R.class)
    @Log(title = "新增字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody DictTypeVO dictTypeVO) {
        return R.ok(dictTypeService.create(BeanUtil.toBean(dictTypeVO, DictTypeDTO.class)));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @ApiOperation(value = "查询字典类型列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<DictTypePageResVO>> page(@Validated @RequestBody DictTypePageVO pageVO) {
        return R.ok(dictTypeService.page(BeanUtil.toBean(pageVO, DictTypePageDTO.class)));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @ApiOperation(value = "修改字典类型", httpMethod = "PUT", response = R.class)
    @Log(title = "修改字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> update(@Validated @RequestBody DictTypeVO dictTypeVO) {
        return R.ok(dictTypeService.update(BeanUtil.toBean(dictTypeVO, DictTypeDTO.class)));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @ApiOperation(value = "删除字典类型", httpMethod = "DELETE", response = R.class)
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping()
    public R<Long> delete(@Validated @RequestBody DictTypeDeleteVO deleteVO) {
        return R.ok(dictTypeService.delete(BeanUtil.toBean(deleteVO, DictTypeDeleteDTO.class)));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @ApiOperation(value = "查询字典类型详细", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{dictId}")
    public R<DictTypeResVO> getInfo(@PathVariable Long dictId) {
        return R.ok(BeanUtil.toBean(dictTypeService.selectById(dictId), DictTypeResVO.class));
    }

    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @ApiOperation(value = "刷新字典缓存", httpMethod = "DELETE", response = R.class)
    @Log(title = "刷新字典缓存", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R refreshCache() {
        dictTypeService.resetDictCache();
        return R.ok();
    }

    @ApiOperation(value = "获取字典选择框列表", httpMethod = "DELETE", response = R.class)
    @GetMapping("/optionselect")
    public R<List<DictTypeResVO>> optionSelect() {
        return R.ok(BeanUtil.copyToList(dictTypeService.selectAll(), DictTypeResVO.class));
    }

}
