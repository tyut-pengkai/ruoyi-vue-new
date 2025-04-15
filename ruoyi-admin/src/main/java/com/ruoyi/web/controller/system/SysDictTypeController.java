package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.domain.vo.dictType.*;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.dto.dictType.DictTypeDTO;
import com.ruoyi.system.domain.dto.dictType.DictTypeDeleteDTO;
import com.ruoyi.system.domain.dto.dictType.DictTypePageDTO;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.SysDictTypeMapper;
import com.ruoyi.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@Api(tags = "系统基础数据 - 字典类型")
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {

    final ISysDictTypeService dictTypeService;

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @ApiOperation(value = "新增字典类型", httpMethod = "POST", response = R.class)
    @Log(title = "新增字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody DictTypeVO dictTypeVO) {
        return R.ok(dictTypeService.create(BeanUtil.toBean(dictTypeVO, DictTypeDTO.class)));
    }

    /**
     * 查询字典类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @ApiOperation(value = "查询字典类型列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<DictTypePageResVO>> page(@Validated @RequestBody DictTypePageVO pageVO) {
        return R.ok(dictTypeService.page(BeanUtil.toBean(pageVO, DictTypePageDTO.class)));
    }

    /**
     * 修改字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @ApiOperation(value = "修改字典类型", httpMethod = "PUT", response = R.class)
    @Log(title = "修改字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> update(@Validated @RequestBody DictTypeVO dictTypeVO) {
        return R.ok(dictTypeService.update(BeanUtil.toBean(dictTypeVO, DictTypeDTO.class)));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @ApiOperation(value = "删除字典类型", httpMethod = "DELETE", response = R.class)
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping()
    public R<Integer> delete(@Validated @RequestBody DictTypeDeleteVO deleteVO) {
        return R.ok(dictTypeService.delete(BeanUtil.toBean(deleteVO, DictTypeDeleteDTO.class)));
    }

    /**
     * 查询字典类型详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @ApiOperation(value = "查询字典类型详细", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{dictId}")
    public R<DictTypeResVO> getInfo(@PathVariable Long dictId) {
        return R.ok(BeanUtil.toBean(dictTypeService.selectById(dictId), DictTypeResVO.class));
    }

    /**
     * 刷新字典缓存
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @ApiOperation(value = "刷新字典缓存", httpMethod = "DELETE", response = R.class)
    @Log(title = "刷新字典缓存", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R refreshCache() {
        dictTypeService.resetDictCache();
        return R.ok();
    }

    /**
     * 获取字典选择框列表
     */
    @ApiOperation(value = "获取字典选择框列表", httpMethod = "DELETE", response = R.class)
    @GetMapping("/optionselect")
    public R<List<DictTypeResVO>> optionSelect() {
        return R.ok(BeanUtil.copyToList(dictTypeService.selectAll(), DictTypeResVO.class));
    }

    final SysDictTypeMapper typeMapper;
    final SysDictDataMapper dataMapper;

    @PostMapping("/importData")
    @Transactional
    public R importData(MultipartFile file) throws Exception {
        final String importType = "fashion_elements";
        final String nameA = "流行元素";
        SysDictType dictType = new SysDictType();
        dictType.setDictType(importType);
        dictType.setDictName(nameA);
        dictType.setStatus("0");
        dictType.setRemark(nameA);
        dictType.setCreateBy("admin");
        typeMapper.insert(dictType);

        ExcelUtil<SysDictData> util = new ExcelUtil<>(SysDictData.class);
        List<SysDictData> list = util.importExcel(file.getInputStream());
        List<String> dataList = list.stream().map(SysDictData::getDictValue).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<SysDictData> insertList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            SysDictData data = new SysDictData();
            data.setDictSort((long) i + 1);
            data.setDictValue(dataList.get(i));
            data.setDictLabel(dataList.get(i));
            data.setDictType(importType);
            data.setStatus("0");
            data.setCreateBy("admin");
            data.setRemark(nameA + "子项");
            insertList.add(data);
        }
        this.dataMapper.insert(insertList);
        return R.ok();
    }




    // ============================================================================================================
    // ============================================================================================================
    // ============================================================================================================
    // ============================================================================================================
















    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDictType dictType) {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }

    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        util.exportExcel(response, list, "字典类型");
    }

    /**
     * 查询字典类型详细
     */
    /*@PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable Long dictId) {
        return success(dictTypeService.selectDictTypeById(dictId));
    }*/

    /**
     * 新增字典类型
     */
    /*@PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(getUsername());
        return toAjax(dictTypeService.insertDictType(dict));
    }*/

    /**
     * 修改字典类型
     */
    /*@PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(getUsername());
        return toAjax(dictTypeService.updateDictType(dict));
    }*/

    /**
     * 删除字典类型
     */
    /*@PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }*/

    /**
     * 刷新字典缓存
     */
    /*@PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        dictTypeService.resetDictCache();
        return success();
    }*/

    /**
     * 获取字典选择框列表
     */
    /*@GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return success(dictTypes);
    }*/

}
