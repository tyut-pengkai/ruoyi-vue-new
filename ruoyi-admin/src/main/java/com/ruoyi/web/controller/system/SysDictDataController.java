package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.vo.dictData.DictDataDeleteVO;
import com.ruoyi.common.core.domain.vo.dictData.DictDataResVO;
import com.ruoyi.common.core.domain.vo.dictData.DictDataVO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.dto.dictData.DictDataDTO;
import com.ruoyi.system.domain.dto.dictData.DictDataDeleteDTO;
import com.ruoyi.system.service.ISysDictDataService;
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
@RequiredArgsConstructor
@Api(tags = "系统基础数据 - 字典数据明细")
@RestController
@RequestMapping("/rest/v1/sys/dict/data")
public class SysDictDataController extends BaseController {

    final ISysDictDataService dictDataService;
    final ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "新增字典明细类型", httpMethod = "POST", response = R.class)
    @Log(title = "新增字典明细类型", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody DictDataVO dataVO) {
        return R.ok(dictDataService.create(BeanUtil.toBean(dataVO, DictDataDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "修改字典明细类型", httpMethod = "PUT", response = R.class)
    @Log(title = "修改字典明细类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> update(@Validated @RequestBody DictDataVO dataVO) {
        return R.ok(dictDataService.update(BeanUtil.toBean(dataVO, DictDataDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "删除字典明细类型", httpMethod = "PUT", response = R.class)
    @Log(title = "删除字典明细类型", businessType = BusinessType.DELETE)
    @DeleteMapping()
    public R<Integer> delete(@Validated @RequestBody DictDataDeleteVO deleteVO) {
        return R.ok(dictDataService.delete(BeanUtil.toBean(deleteVO, DictDataDeleteDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "查询字典数据详细", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{dictDataId}")
    public R<DictDataResVO> getInfo(@PathVariable Long dictDataId) {
        return R.ok(BeanUtil.toBean(dictDataService.selectById(dictDataId), DictDataResVO.class));
    }

    @GetMapping(value = "/type/{dictType}")
    @ApiOperation(value = "根据字典类型查询字典数据信息", httpMethod = "GET", response = R.class)
    public R<List<DictDataResVO>> dictType(@PathVariable String dictType) {
        return R.ok(BeanUtil.copyToList(dictDataService.selectByDictType(dictType), DictDataResVO.class));
    }

}
