package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.vo.dictData.DictDataDeleteVO;
import com.ruoyi.common.core.domain.vo.dictData.DictDataPageVO;
import com.ruoyi.common.core.domain.vo.dictData.DictDataResVO;
import com.ruoyi.common.core.domain.vo.dictData.DictDataVO;
import com.ruoyi.common.core.domain.vo.dictType.DictTypePageResVO;
import com.ruoyi.common.core.domain.vo.dictType.DictTypePageVO;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.dto.dictData.DictDataDTO;
import com.ruoyi.system.domain.dto.dictData.DictDataDeleteDTO;
import com.ruoyi.system.domain.dto.dictData.DictDataPageDTO;
import com.ruoyi.system.domain.dto.dictData.DictDataResDTO;
import com.ruoyi.system.domain.dto.dictType.DictTypePageDTO;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Api(tags = "系统基础数据 - 字典数据明细")
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {

    final ISysDictDataService dictDataService;
    final ISysDictTypeService dictTypeService;

    /**
     * 新增字典明细类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @ApiOperation(value = "新增字典明细类型", httpMethod = "POST", response = R.class)
    @Log(title = "新增字典明细类型", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody DictDataVO dataVO) {
        return R.ok(dictDataService.create(BeanUtil.toBean(dataVO, DictDataDTO.class)));
    }

    /**
     * 修改字典明细类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @ApiOperation(value = "修改字典明细类型", httpMethod = "PUT", response = R.class)
    @Log(title = "修改字典明细类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> update(@Validated @RequestBody DictDataVO dataVO) {
        return R.ok(dictDataService.update(BeanUtil.toBean(dataVO, DictDataDTO.class)));
    }

    /**
     * 删除字典明细类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @ApiOperation(value = "删除字典明细类型", httpMethod = "PUT", response = R.class)
    @Log(title = "删除字典明细类型", businessType = BusinessType.DELETE)
    @DeleteMapping()
    public R<Integer> delete(@Validated @RequestBody DictDataDeleteVO deleteVO) {
        return R.ok(dictDataService.delete(BeanUtil.toBean(deleteVO, DictDataDeleteDTO.class)));
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @ApiOperation(value = "查询字典数据详细", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{dictDataId}")
    public R<DictDataResVO> getInfo(@PathVariable Long dictDataId) {
        return R.ok(BeanUtil.toBean(dictDataService.selectById(dictDataId), DictDataResVO.class));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    @ApiOperation(value = "根据字典类型查询字典数据信息", httpMethod = "GET", response = R.class)
    public R<List<DictDataResVO>> dictType(@PathVariable String dictType) {
        return R.ok(BeanUtil.copyToList(dictDataService.selectByDictType(dictType), DictDataResVO.class));
    }




































    // ===============================================================================================
    // ===============================================================================================
    // ===============================================================================================
    // ===============================================================================================


    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
   /* @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return success(dictDataService.selectDictDataById(dictCode));
    }*/

    /**
     * 根据字典类型查询字典数据信息
     */
   /* @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return success(data);
    }*/

    /**
     * 新增字典类型
     */
    /*@PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(getUsername());
        return toAjax(dictDataService.insertDictData(dict));
    }*/

    /**
     * 修改保存字典类型
     */
    /*@PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(getUsername());
        return toAjax(dictDataService.updateDictData(dict));
    }*/

    /**
     * 删除字典类型
     */
    /*@PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }*/

}
