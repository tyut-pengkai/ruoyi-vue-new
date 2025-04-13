package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.dictData.DictDataDTO;
import com.ruoyi.system.domain.dto.dictData.DictDataDeleteDTO;
import com.ruoyi.system.domain.dto.dictData.DictDataPageDTO;
import com.ruoyi.system.domain.dto.dictData.DictDataResDTO;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {

    final SysDictDataMapper dictDataMapper;

    private final static String STATUS_NORMAL = "0";


    @Override
    @Transactional
    public Integer create(DictDataDTO dataDTO) {
        SysDictData dictData = BeanUtil.toBean(dataDTO, SysDictData.class);
        dictData.setCreateBy(getUsername());
        return this.dictDataMapper.insert(dictData);
    }

    @Override
    @Transactional
    public Integer update(DictDataDTO dataDTO) {
        SysDictData dict = Optional.ofNullable(this.dictDataMapper.selectOne(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getId, dataDTO.getDictDataId()).eq(SysDictData::getDelFlag, Constants.UNDELETED)
                .eq(SysDictData::getStatus, dataDTO.getStatus())))
                .orElseThrow(() -> new ServiceException("字典数据不存在!", HttpStatus.ERROR));
        dict.setUpdateBy(getUsername());
        BeanUtil.copyProperties(dataDTO, dict);
        return this.dictDataMapper.updateById(dict);
    }

    @Override
    @Transactional
    public Integer delete(DictDataDeleteDTO deleteDTO) {
        List<SysDictData> dataList = Optional.ofNullable(this.dictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                        .in(SysDictData::getId, deleteDTO.getDictDataIdList()).eq(SysDictData::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("字典数据不存在!", HttpStatus.ERROR));
        dataList.forEach(x -> x.setDelFlag(Constants.DELETED));
        return this.dictDataMapper.updateById(dataList).size();
    }

    @Override
    @Transactional(readOnly = true)
    public DictDataResDTO selectById(Long dictDataId) {
        SysDictData dictData = Optional.ofNullable(this.dictDataMapper.selectOne(new LambdaQueryWrapper<SysDictData>()
                        .eq(SysDictData::getId, dictDataId).eq(SysDictData::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("字典数据不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(dictData, DictDataResDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DictDataResDTO> selectByDictType(String dictType) {
        List<SysDictData> dataList = this.dictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType).eq(SysDictData::getDelFlag, Constants.UNDELETED));
        return BeanUtil.copyToList(dataList, DictDataResDTO.class);
    }

















    // ===================================================================================================
    // ===================================================================================================
    // ===================================================================================================


    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = selectDictDataById(dictCode);
            dictDataMapper.deleteDictDataById(dictCode);
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData data) {
        int row = dictDataMapper.insertDictData(data);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData data) {
        int row = dictDataMapper.updateDictData(data);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }


}
