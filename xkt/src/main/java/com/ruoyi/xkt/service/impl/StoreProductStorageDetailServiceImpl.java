package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageDetailDownloadDTO;
import com.ruoyi.xkt.dto.storeProdStorage.StoreStorageExportDTO;
import com.ruoyi.xkt.mapper.StoreProductMapper;
import com.ruoyi.xkt.mapper.StoreProductStorageDetailMapper;
import com.ruoyi.xkt.service.IStoreProductStorageDetailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
    final StoreProductMapper storeProdMapper;

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
            return this.storageDetailMapper.selectExportList(exportDTO);
        } else {
            // 没有传时间，则设置当前时间往前推半年
            if (ObjectUtils.isEmpty(exportDTO.getVoucherDateStart()) && ObjectUtils.isEmpty(exportDTO.getVoucherDateEnd())) {
                exportDTO.setVoucherDateEnd(java.sql.Date.valueOf(LocalDate.now()));
                exportDTO.setVoucherDateEnd(java.sql.Date.valueOf(LocalDate.now().minusMonths(6)));
            } else {
                LocalDate start = exportDTO.getVoucherDateStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate end = exportDTO.getVoucherDateEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                // 确保开始日期不大于结束日期
                if (start.isAfter(end)) {
                    throw new ServiceException("开始时间不能晚于结束时间!", HttpStatus.ERROR);
                }
                // 计算两个日期之间的确切月数差异
                long monthsBetween = ChronoUnit.MONTHS.between(start, end);
                // 检查是否超过6个月（允许恰好6个月）
                if (monthsBetween > 6 || (monthsBetween == 6 && start.plusMonths(6).isBefore(end))) {
                    throw new ServiceException("导出时间间隔不能超过6个月!", HttpStatus.ERROR);
                }
            }
            List<StoreProduct> storeProdList = this.storeProdMapper.selectList(new LambdaQueryWrapper<StoreProduct>()
                    .eq(StoreProduct::getStoreId, exportDTO.getStoreId()).eq(StoreProduct::getDelFlag, Constants.UNDELETED));
            exportDTO.setStoreProdIdList(storeProdList.stream().map(StoreProduct::getId).collect(Collectors.toList()));
            return this.storageDetailMapper.selectExportListVoucherDateBetween(exportDTO);
        }
    }
}
