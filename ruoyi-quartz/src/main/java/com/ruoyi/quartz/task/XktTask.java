package com.ruoyi.quartz.task;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysProductCategory;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.quartz.domain.DailySale;
import com.ruoyi.quartz.domain.DailySaleCustomer;
import com.ruoyi.quartz.domain.DailySaleProduct;
import com.ruoyi.quartz.dto.DailySaleCusDTO;
import com.ruoyi.quartz.dto.DailySaleDTO;
import com.ruoyi.quartz.dto.DailySaleProdDTO;
import com.ruoyi.quartz.dto.WeekCateSaleDTO;
import com.ruoyi.quartz.mapper.DailySaleCustomerMapper;
import com.ruoyi.quartz.mapper.DailySaleMapper;
import com.ruoyi.quartz.mapper.DailySaleProductMapper;
import com.ruoyi.quartz.mapper.WeekCateSaleMapper;
import com.ruoyi.system.mapper.SysProductCategoryMapper;
import com.ruoyi.xkt.mapper.StoreProductStorageMapper;
import com.ruoyi.xkt.mapper.StoreSaleDetailMapper;
import com.ruoyi.xkt.mapper.StoreSaleMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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
    final SysProductCategoryMapper prodCateMapper;
    final WeekCateSaleMapper weekCateSaleMapper;

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

    @Transactional
    public void categorySort() {
        // 系统所有的商品分类
        List<SysProductCategory> cateList = this.prodCateMapper.selectList(new LambdaQueryWrapper<SysProductCategory>()
                .eq(SysProductCategory::getDelFlag, Constants.UNDELETED).eq(SysProductCategory::getStatus, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(cateList)) {
            throw new ServiceException("商品分类不存在!", HttpStatus.ERROR);
        }
        // 根据LocalDate 获取当前日期前一天
        final Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 及当前日期前一天的前一周，并转为 Date 格式
        final Date pastDate = Date.from(LocalDate.now().minusDays(8).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // 获取各项子分类最近一周的销售数量
        List<WeekCateSaleDTO> weekCateSaleList = this.weekCateSaleMapper.selectWeekCateSale(yesterday, pastDate);
        if (CollectionUtils.isEmpty(weekCateSaleList)) {
            return;
        }
        // 将各个小项销售数量转化为map
        Map<Long, Integer> itemCateCountMap = weekCateSaleList.stream().collect(Collectors.toMap(WeekCateSaleDTO::getProdCateId, WeekCateSaleDTO::getCount));
        // 按照大类对应的各小类以此进行数量统计及排序
        List<WeekCateSaleDTO> sortList = new ArrayList<>();
        cateList.stream()
                // 过滤掉父级为0的分类，以及父级为1的分类（父级为1的为子分类）
                .filter(x -> !Objects.equals(x.getParentId(), 0L) && !Objects.equals(x.getParentId(), 1L))
                .collect(Collectors.groupingBy(SysProductCategory::getParentId))
                .forEach((parentId, itemList) -> sortList.add(new WeekCateSaleDTO() {{
                    setCount(itemList.stream().mapToInt(x -> itemCateCountMap.getOrDefault(x.getId(), 0)).sum());
                    setProdCateId(parentId);
                }}));
        // 按照大类的数量倒序排列
        sortList.sort(Comparator.comparing(WeekCateSaleDTO::getCount).reversed());
        Map<Long, Integer> topCateSortMap = new LinkedHashMap<>();
        // 按照sortList的顺序，结合 topCateMap 依次更新SysProductCategory 的 sortNum的排序
        for (int i = 0; i < sortList.size(); i++) {
            topCateSortMap.put(sortList.get(i).getProdCateId(), i + 1);
        }
        // 顶级分类的数量
        Integer topCateSize = Math.toIntExact(cateList.stream().filter(x -> Objects.equals(x.getParentId(), 1L)).count());
        // 次级分类列表
        List<SysProductCategory> updateList = cateList.stream().filter(x -> Objects.equals(x.getParentId(), 1L))
                // 如果存在具体销售数量，则按照具体销售数量排序，否则将排序置为最大值
                .peek(x -> x.setOrderNum(topCateSortMap.getOrDefault(x.getId(), topCateSize)))
                .collect(Collectors.toList());
        this.prodCateMapper.updateById(updateList);
    }

}



