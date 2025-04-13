package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.domain.vo.dictType.DictTypePageResVO;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.dictType.DictTypeDTO;
import com.ruoyi.system.domain.dto.dictType.DictTypeDeleteDTO;
import com.ruoyi.system.domain.dto.dictType.DictTypePageDTO;
import com.ruoyi.system.domain.dto.dictType.DictTypeResDTO;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.SysDictTypeMapper;
import com.ruoyi.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl implements ISysDictTypeService {

    final SysDictTypeMapper dictTypeMapper;
    final SysDictDataMapper dictDataMapper;

    private final static String STATUS_NORMAL = "0";

    /**
     * 检查字典类型的唯一性
     *
     * 此方法用于确保系统中每个字典类型的唯一性它通过比较数据库中同名字典类型的的存在情况，
     * 以及是否与当前字典ID匹配，来判断字典类型是否唯一
     *
     * @param typeDTO 字典类型的视图对象，包含需要检查的字典类型信息
     * @return 返回一个布尔值，表示字典类型是否唯一
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkDictTypeUnique(DictTypeDTO typeDTO) {
        // 初始化字典ID，默认值为-1，用于后续比较
        Long dictId = ObjectUtils.defaultIfNull(typeDTO.getDictId(), -1L);
        // 通过字典类型查询数据库，检查是否存在重复的字典类型
        SysDictType dictType = dictTypeMapper.checkDictTypeUnique(typeDTO.getDictType());
        // 判断查询结果是否非空且字典ID不匹配，如果不匹配则表示字典类型不唯一
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        // 如果查询结果为空或字典ID匹配，表示字典类型唯一
        return UserConstants.UNIQUE;
    }

    /**
     * 新增字典类型
     *
     * @param typeDTO 新增类型DTO
     * @return Integer
     */
    @Override
    @Transactional
    public Integer create(DictTypeDTO typeDTO) {
        // 如果字典名称已存在,则报错
        if (!this.checkDictTypeUnique(typeDTO)) {
            throw new ServiceException("新增字典'" + typeDTO.getDictName() + "'失败，字典类型已存在", HttpStatus.ERROR);
        }
        SysDictType dict = BeanUtil.toBean(typeDTO, SysDictType.class);
        dict.setStatus(STATUS_NORMAL);
        dict.setCreateBy(getUsername());
        return this.dictTypeMapper.insert(dict);
    }

    /**
     * 更新字典类型
     *
     * @param typeDTO 更新字典类型入参
     * @return
     */
    @Override
    @Transactional
    public Integer update(DictTypeDTO typeDTO) {
        // 编辑时dictId不可为空
        Optional.ofNullable(typeDTO.getDictId()).orElseThrow(() -> new ServiceException("字典ID不能为空!", HttpStatus.ERROR));
        if (!this.checkDictTypeUnique(typeDTO)) {
            throw new ServiceException("修改字典'" + typeDTO.getDictName() + "'失败，字典类型已存在", HttpStatus.ERROR);
        }
        SysDictType dict = Optional.ofNullable(this.dictTypeMapper.selectOne(new LambdaQueryWrapper<SysDictType>()
                        .eq(SysDictType::getDictId, typeDTO.getDictId()).eq(SysDictType::getDelFlag, Constants.UNDELETED)
                        .eq(SysDictType::getStatus, typeDTO.getStatus())))
                .orElseThrow(() -> new ServiceException("字典类型不存在!", HttpStatus.ERROR));
        dict.setUpdateBy(getUsername());
        BeanUtil.copyProperties(typeDTO, dict);
        return this.dictTypeMapper.updateById(dict);
    }

    /**
     * 删除字典类型
     *
     * @param deleteDTO 删除字典类型入参
     * @return Integer
     */
    @Override
    @Transactional
    public Integer delete(DictTypeDeleteDTO deleteDTO) {
        List<SysDictType> dictList = Optional.ofNullable(this.dictTypeMapper.selectList(new LambdaQueryWrapper<SysDictType>()
                        .in(SysDictType::getDictId, deleteDTO.getDictIdList()).eq(SysDictType::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("字典类型不存在!", HttpStatus.ERROR));
        dictList.forEach(x -> x.setDelFlag(Constants.DELETED));
        return this.dictTypeMapper.updateById(dictList).size();
    }


    /**
     * 用户字典分页查询
     *
     * @param pageDTO 分页查询入参
     * @return Page<DictTypePageResVO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DictTypePageResVO> page(DictTypePageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDelFlag, Constants.UNDELETED);
        if (StringUtils.isNotBlank(pageDTO.getDictName())) {
            queryWrapper.like(SysDictType::getDictName, pageDTO.getDictName());
        }
        if (StringUtils.isNotBlank(pageDTO.getDictType())) {
            queryWrapper.like(SysDictType::getDictType, pageDTO.getDictType());
        }
        if (StringUtils.isNotBlank(pageDTO.getStatus())) {
            queryWrapper.eq(SysDictType::getStatus, pageDTO.getStatus());
        }
        List<SysDictType> list = this.dictTypeMapper.selectList(queryWrapper);
        return Page.convert(new PageInfo<>(list), BeanUtil.copyToList(list, DictTypePageResVO.class));
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    @Transactional(readOnly = true)
    public List<DictTypeResDTO> selectAll() {
        List<SysDictType> dictList = this.dictTypeMapper.selectList(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getStatus, STATUS_NORMAL).eq(SysDictType::getDelFlag, Constants.UNDELETED));
        return BeanUtil.copyToList(dictList, DictTypeResDTO.class);
    }

    /**
     * 获取字典数据详情
     *
     * @param dictId 字典ID
     * @return DictTypeResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public DictTypeResDTO selectById(Long dictId) {
        SysDictType dict = Optional.ofNullable(this.dictTypeMapper.selectOne(new LambdaQueryWrapper<SysDictType>()
                        .eq(SysDictType::getDictId, dictId).eq(SysDictType::getDelFlag, Constants.UNDELETED)
                        .eq(SysDictType::getStatus, STATUS_NORMAL)))
                .orElseThrow(() -> new ServiceException("字典类型不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(dict, DictTypeResDTO.class);
    }


    // =======================================================================================================
    // =======================================================================================================
    // =======================================================================================================
    // =======================================================================================================
    // =======================================================================================================








































    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        return dictTypeMapper.selectDictTypeList(dictType);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return dictTypeMapper.selectDictTypeAll();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = dictDataMapper.selectDictDataByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return dictTypeMapper.selectDictTypeById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        return dictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            dictTypeMapper.deleteDictTypeById(dictId);
            DictUtils.removeDictCache(dictType.getDictType());
        }
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        SysDictData dictData = new SysDictData();
        dictData.setStatus("0");
        Map<String, List<SysDictData>> dictDataMap = dictDataMapper.selectDictDataList(dictData).stream().collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(SysDictType dict) {
        int row = dictTypeMapper.insertDictType(dict);
        if (row > 0) {
            DictUtils.setDictCache(dict.getDictType(), null);
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictType(SysDictType dict) {
        SysDictType oldDict = dictTypeMapper.selectDictTypeById(dict.getDictId());
        dictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        int row = dictTypeMapper.updateDictType(dict);
        if (row > 0) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dict.getDictType());
            DictUtils.setDictCache(dict.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public boolean checkDictTypeUnique(SysDictType dict) {
        Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        SysDictType dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


}
