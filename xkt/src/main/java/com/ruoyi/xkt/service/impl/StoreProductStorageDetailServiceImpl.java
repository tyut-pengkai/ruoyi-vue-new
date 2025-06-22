package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageDetailDownloadDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageExportDTO;
import com.ruoyi.xkt.mapper.StoreProductStorageDetailMapper;
import com.ruoyi.xkt.service.IStoreProductStorageDetailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口商品入库明细Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreProductStorageDetailServiceImpl implements IStoreProductStorageDetailService {

    final StoreProductStorageDetailMapper storageDetailMapper;

    /**
     * 导出档口商品入库明细
     *
     * @param exportDTO 导出参数
     * @return 导出数据
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreStorageDetailDownloadDTO> export(StoreStorageExportDTO exportDTO) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(exportDTO.getStoreId())) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        if (CollectionUtils.isNotEmpty(exportDTO.getStoreProdStorageIdList())) {
            return this.storageDetailMapper.selectExportList(exportDTO.getStoreProdStorageIdList());
        } else {
            if (ObjectUtils.isEmpty(exportDTO.getVoucherDateStart()) && ObjectUtils.isEmpty(exportDTO.getVoucherDateEnd())) {
                throw new ServiceException("全量导出时，开始时间和结束时间不能为空!", HttpStatus.ERROR);
            }
            return this.storageDetailMapper.selectExportListVoucherDateBetween(exportDTO.getVoucherDateStart(), exportDTO.getVoucherDateEnd());
        }
    }
}
