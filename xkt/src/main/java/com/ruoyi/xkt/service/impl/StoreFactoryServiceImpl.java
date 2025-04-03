package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreFactory;
import com.ruoyi.xkt.dto.storeFactory.StoreFactoryDTO;
import com.ruoyi.xkt.dto.storeFactory.StoreFactoryPageDTO;
import com.ruoyi.xkt.dto.storeFactory.StoreFactoryResDTO;
import com.ruoyi.xkt.mapper.StoreFactoryMapper;
import com.ruoyi.xkt.service.IStoreFactoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 档口合作工厂Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreFactoryServiceImpl implements IStoreFactoryService {

    final StoreFactoryMapper storeFactoryMapper;

    /**
     * 新增档口合作工厂
     *
     * @param storeFactoryDTO 档口合作工厂
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreFactory(StoreFactoryDTO storeFactoryDTO) {
        StoreFactory storeFactory = BeanUtil.toBean(storeFactoryDTO, StoreFactory.class);
        return storeFactoryMapper.insert(storeFactory);
    }

    /**
     * 修改档口合作工厂
     *
     * @param storeFactoryDTO 档口合作工厂
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreFactory(StoreFactoryDTO storeFactoryDTO) {
        if (ObjectUtils.isEmpty(storeFactoryDTO.getStoreFactoryId())) {
            throw new ServiceException("storeFactoryId不可为空!", HttpStatus.ERROR);
        }
        StoreFactory storeFactory = Optional.ofNullable(this.storeFactoryMapper.selectOne(new LambdaQueryWrapper<StoreFactory>()
                        .eq(StoreFactory::getId, storeFactoryDTO.getStoreFactoryId()).eq(StoreFactory::getDelFlag, "0")))
                .orElseThrow(() -> new ServiceException("档口合作工厂不存在!", HttpStatus.ERROR));
        BeanUtil.copyProperties(storeFactoryDTO, storeFactory);
        return storeFactoryMapper.updateById(storeFactory);
    }

    /**
     * 重写selectFactoryPage方法，用于查询工厂页面信息
     *
     * @param pageDTO 页面查询条件，包含商店ID、工厂名称、页码和页面大小
     * @return 返回一个分页结果对象，包含查询到的工厂信息列表
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreFactoryResDTO> selectFactoryPage(StoreFactoryPageDTO pageDTO) {
        // 创建查询条件对象，用于构建SQL查询语句
        LambdaQueryWrapper<StoreFactory> queryWrapper = new LambdaQueryWrapper<StoreFactory>()
                .eq(StoreFactory::getStoreId, pageDTO.getStoreId()).eq(StoreFactory::getDelFlag, "0");
        // 如果工厂名称不为空，则添加模糊查询条件
        if (StringUtils.isNotBlank(pageDTO.getFacName())) {
            queryWrapper.like(StoreFactory::getFacName, pageDTO.getFacName());
        }
        // 启用MyBatis分页插件，设置当前页码和页面大小
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        // 执行查询，获取工厂列表
        List<StoreFactory> list = this.storeFactoryMapper.selectList(queryWrapper);
        // 将查询结果转换为分页信息对象，并将工厂信息列表转换为DTO列表返回
        return Page.convert(new PageInfo<>(list), BeanUtil.copyToList(list, StoreFactoryResDTO.class));
    }

    /**
     * 查询档口合作工厂
     *
     * @param storeFacId 档口合作工厂主键
     * @return 档口合作工厂
     */
    @Override
    @Transactional(readOnly = true)
    public StoreFactoryResDTO selectByStoreFacId(Long storeId, Long storeFacId) {
        StoreFactory storeFactory = Optional.ofNullable(this.storeFactoryMapper.selectOne(new LambdaQueryWrapper<StoreFactory>()
                .eq(StoreFactory::getId, storeFacId).eq(StoreFactory::getDelFlag, "0").eq(StoreFactory::getStoreId, storeId)))
                .orElseThrow(() -> new ServiceException("档口合作工厂不存在!", HttpStatus.ERROR));
        return BeanUtil.toBean(storeFactory, StoreFactoryResDTO.class);
    }








    /**
     * 查询档口合作工厂列表
     *
     * @param storeFactory 档口合作工厂
     * @return 档口合作工厂
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreFactory> selectStoreFactoryList(StoreFactory storeFactory) {
        return storeFactoryMapper.selectStoreFactoryList(storeFactory);
    }




    /**
     * 批量删除档口合作工厂
     *
     * @param storeFacIds 需要删除的档口合作工厂主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreFactoryByStoreFacIds(Long[] storeFacIds) {
        return storeFactoryMapper.deleteStoreFactoryByStoreFacIds(storeFacIds);
    }

    /**
     * 删除档口合作工厂信息
     *
     * @param storeFacId 档口合作工厂主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreFactoryByStoreFacId(Long storeFacId) {
        return storeFactoryMapper.deleteStoreFactoryByStoreFacId(storeFacId);
    }


}
