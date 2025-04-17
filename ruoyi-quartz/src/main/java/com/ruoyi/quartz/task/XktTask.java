package com.ruoyi.quartz.task;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.quartz.domain.DailySale;
import com.ruoyi.quartz.domain.DailySaleCustomer;
import com.ruoyi.quartz.domain.DailySaleProduct;
import com.ruoyi.quartz.dto.DailySaleCusDTO;
import com.ruoyi.quartz.dto.DailySaleDTO;
import com.ruoyi.quartz.dto.DailySaleProdDTO;
import com.ruoyi.quartz.mapper.DailySaleCustomerMapper;
import com.ruoyi.quartz.mapper.DailySaleMapper;
import com.ruoyi.quartz.mapper.DailySaleProductMapper;
import com.ruoyi.xkt.mapper.StoreProductStorageMapper;
import com.ruoyi.xkt.mapper.StoreSaleDetailMapper;
import com.ruoyi.xkt.mapper.StoreSaleMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 鞋库通定时任务
 *
 * @author ruoyi
 */
@Component("xktTask")
@RequiredArgsConstructor
public class XktTask {

    final DailySaleMapper dailySaleMapper;
    final StoreSaleMapper saleMapper;
    final StoreSaleDetailMapper saleDetailMapper;
    final StoreProductStorageMapper storageMapper;
    final DailySaleCustomerMapper dailySaleCusMapper;
    final DailySaleProductMapper dailySaleProdMapper;

    /**
     * 每晚1点同步档口销售数据
     */
    @Transactional
    public void dailySale() {
        // 使用LocalDate获取当前日期前一天，并转为 Date 格式
        final Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 先检查是否有该天的销售数据，若有则先删除，保证数据唯一性
        List<DailySale> existList = this.dailySaleMapper.selectList(new LambdaQueryWrapper<DailySale>()
                .eq(DailySale::getVoucherDate, yesterday).eq(DailySale::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(existList)) {
            this.dailySaleMapper.deleteByIds(existList.stream().map(DailySale::getId).collect(Collectors.toList()));
        }
        // 查询当前的销售数据
        List<DailySaleDTO> saleList = this.dailySaleMapper.selectDailySale(yesterday);
        if (CollectionUtils.isEmpty(saleList)) {
            return;
        }
        this.dailySaleMapper.insert(saleList.stream().map(x -> BeanUtil.toBean(x, DailySale.class)
                .setVoucherDate(yesterday)).collect(Collectors.toList()));
    }

    /**
     * 每晚1点30分同步档口客户销售数据
     */
    @Transactional
    public void dailySaleCustomer() {
        // 使用LocalDate获取当前日期前一天，并转为 Date 格式
        final Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 先检查是否有该天的销售数据，若有则先删除，保证数据唯一性
        List<DailySaleCustomer> existList = this.dailySaleCusMapper.selectList(new LambdaQueryWrapper<DailySaleCustomer>()
                .eq(DailySaleCustomer::getVoucherDate, yesterday).eq(DailySaleCustomer::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(existList)) {
            this.dailySaleCusMapper.deleteByIds(existList.stream().map(DailySaleCustomer::getId).collect(Collectors.toList()));
        }
        // 查询当前的客户销售数据
        List<DailySaleCusDTO> cusSaleList = this.dailySaleCusMapper.selectDailySale(yesterday);
        if (CollectionUtils.isEmpty(cusSaleList)) {
            return;
        }
        this.dailySaleCusMapper.insert(cusSaleList.stream().map(x -> BeanUtil.toBean(x, DailySaleCustomer.class)
                .setVoucherDate(yesterday)).collect(Collectors.toList()));
    }

    /**
     * 每晚2点同步档口商品销售数据
     */
    @Transactional
    public void dailySaleProduct() {
        // 使用LocalDate获取当前日期前一天，并转为 Date 格式
        final Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 先检查是否有该天的销售数据，若有则先删除，保证数据唯一性
        List<DailySaleProduct> existList = this.dailySaleProdMapper.selectList(new LambdaQueryWrapper<DailySaleProduct>()
                .eq(DailySaleProduct::getVoucherDate, yesterday).eq(DailySaleProduct::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isNotEmpty(existList)) {
            this.dailySaleProdMapper.deleteByIds(existList.stream().map(DailySaleProduct::getId).collect(Collectors.toList()));
        }
        // 查询档口商品的销售数据
        List<DailySaleProdDTO> saleList = this.dailySaleProdMapper.selectDailySale(yesterday);
        if (CollectionUtils.isEmpty(saleList)) {
            return;
        }
        this.dailySaleProdMapper.insert(saleList.stream().map(x -> BeanUtil.toBean(x, DailySaleProduct.class)
                .setVoucherDate(yesterday)).collect(Collectors.toList()));

    }

}
