package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.domain.StoreHomepage;
import com.ruoyi.xkt.domain.SysFile;
import com.ruoyi.xkt.dto.storeHomepage.StoreHomeDTO;
import com.ruoyi.xkt.mapper.StoreHomepageMapper;
import com.ruoyi.xkt.mapper.StoreMapper;
import com.ruoyi.xkt.mapper.SysFileMapper;
import com.ruoyi.xkt.service.IStoreHomepageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 档口首页Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RequiredArgsConstructor
@Service
public class StoreHomepageServiceImpl implements IStoreHomepageService {

    final SysFileMapper fileMapper;
    final StoreHomepageMapper storeHomeMapper;
    final StoreMapper storeMapper;


    /**
     * 新增档口首页各部分图
     *
     * @param storeId     档口ID
     * @param templateNum 使用的模板No
     * @param homepageDTO 新增档口首页各部分图
     * @return Integer
     */
    @Override
    @Transactional
    public Integer insert(Long storeId, Integer templateNum, StoreHomeDTO homepageDTO) {
        Integer count = this.insertToSysFile(storeId, homepageDTO);
        // 当前档口首页各部分总的文件大小
        BigDecimal totalSize = homepageDTO.getFileList().stream().map(x -> ObjectUtils.defaultIfNull(x.getFileSize(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>().eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setTemplateNum(templateNum);
        // 更新档口首页使用的总的文件容量
        store.setStorageUsage(ObjectUtils.defaultIfNull(store.getStorageUsage(), BigDecimal.ZERO).add(totalSize));
        this.storeMapper.updateById(store);
        return count;
    }


    /**
     * 获取档口首页各个部分的图信息
     *
     * @param storeId 档口ID
     * @return StoreHomeDTO
     */
    @Override
    @Transactional(readOnly = true)
    public StoreHomeDTO selectByStoreId(Long storeId) {
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        List<StoreHomepage> homeList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED));
        List<SysFile> fileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                        .in(SysFile::getId, homeList.stream().map(StoreHomepage::getFileId).collect(Collectors.toList()))
                        .eq(SysFile::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("文件不存在", HttpStatus.ERROR));
        Map<Long, SysFile> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getId, x -> x));
        return new StoreHomeDTO() {{
            setTemplateNum(store.getTemplateNum());
            setFileList(homeList.stream().map(x -> BeanUtil.toBean(x, StoreHomeDTO.HomePageFileDTO.class)
                            .setFileType(x.getType()).setFileName(fileMap.get(x.getFileId()).getFileName())
                            .setFileUrl(fileMap.get(x.getFileId()).getFileUrl()).setFileSize(fileMap.get(x.getFileId()).getFileSize()))
                    .collect(Collectors.toList()));
        }};
    }

    /**
     * 更新档口首页各部分图信息
     *
     * @param storeId     档口ID
     * @param templateNum 选择的模板Num
     * @param homeDTO     更新的dto
     * @return Integer
     */
    @Override
    @Transactional
    public Integer updateStoreHomepage(Long storeId, Integer templateNum, StoreHomeDTO homeDTO) {
        // 先将所有的档口模板的文件都删除掉
        List<StoreHomepage> oldHomeList = this.storeHomeMapper.selectList(new LambdaQueryWrapper<StoreHomepage>()
                .eq(StoreHomepage::getStoreId, storeId).eq(StoreHomepage::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(oldHomeList)) {
            oldHomeList.forEach(x -> x.setDelFlag(Constants.DELETED));
            this.storeHomeMapper.updateById(oldHomeList);
        }
        // 新增档口首页各部分图
        Integer count = this.insertToSysFile(storeId, homeDTO);
        List<SysFile> fileList = Optional.ofNullable(this.fileMapper.selectList(new LambdaQueryWrapper<SysFile>()
                        .in(SysFile::getId, oldHomeList.stream().map(StoreHomepage::getFileId).collect(Collectors.toList()))
                        .eq(SysFile::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("文件不存在", HttpStatus.ERROR));
        // 旧的档口首页各部分文件大小
        BigDecimal oldTotalSize = fileList.stream().map(x -> ObjectUtils.defaultIfNull(x.getFileSize(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 新的文件大小
        BigDecimal newTotalSize = homeDTO.getFileList().stream().map(x -> ObjectUtils.defaultIfNull(x.getFileSize(), BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 新的文件大小和旧的文件大小的差值
        BigDecimal diffFileSize = newTotalSize.subtract(oldTotalSize);
        Store store = Optional.ofNullable(this.storeMapper.selectOne(new LambdaQueryWrapper<Store>()
                        .eq(Store::getId, storeId).eq(Store::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("档口不存在!", HttpStatus.ERROR));
        store.setTemplateNum(templateNum);
        store.setStorageUsage(ObjectUtils.defaultIfNull(store.getStorageUsage(), BigDecimal.ZERO).add(diffFileSize));
        this.storeMapper.updateById(store);
        return count;
    }


    /**
     * 将数据插入到系统文件表中
     *
     * @param storeId     档口ID
     * @param homepageDTO 文件DTO
     * @return 插入的数量
     */
    private Integer insertToSysFile(Long storeId, StoreHomeDTO homepageDTO) {
        // 将文件插入到SysFile表中
        List<SysFile> fileList = BeanUtil.copyToList(homepageDTO.getFileList(), SysFile.class);
        this.fileMapper.insert(fileList);
        // 将文件名称和文件ID映射到Map中
        Map<String, Long> fileMap = fileList.stream().collect(Collectors.toMap(SysFile::getFileName, SysFile::getId));
        List<StoreHomepage> homepageList = homepageDTO.getFileList().stream().map(x -> BeanUtil.toBean(x, StoreHomepage.class).setStoreId(storeId).setType(x.getFileType())
                        .setFileId(Optional.ofNullable(fileMap.get(x.getFileName())).orElseThrow(() -> new ServiceException("文件不存在", HttpStatus.ERROR))))
                .collect(Collectors.toList());
        return this.storeHomeMapper.insert(homepageList).size();
    }

}
