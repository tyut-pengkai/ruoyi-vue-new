package com.ruoyi.xkt.service.impl;

import com.ruoyi.xkt.dto.advertRoundPlay.AdPlayStoreCreateDTO;
import com.ruoyi.xkt.dto.advertRoundPlay.AdPlayStoreResDTO;
import com.ruoyi.xkt.mapper.AdvertRoundPlayMapper;
import com.ruoyi.xkt.service.IAdvertRoundPlayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 推广营销轮次播放Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class AdvertRoundPlayServiceImpl implements IAdvertRoundPlayService {

    final AdvertRoundPlayMapper adRoundPlayMapper;

    /**
     * 获取当前类型下档口的推广营销数据
     *
     * @param storeId 档口ID
     * @param typeId  推广类型ID
     * @return AdRoundPlayStoreResDTO
     */
    @Override
    @Transactional(readOnly = true)
    public AdPlayStoreResDTO getStoreAdInfo(Long storeId, Integer typeId) {
        // 先获取所有 投放中 待投放的营销推广
        // 再判断当前当前与每一轮推广营销中的关系，已出价、竞价失败、竞价成功等

        return null;
    }

    /**
     * 档口购买推广营销
     * 思路：每次筛选出某个类型，价格最低的推广位，然后只操作这行数据。
     *      若：该行数据已经有其它档口先竞价了。先进行比价。如果出价低于最低价格，则抛出异常：“已经有档口出价更高了噢，请重新出价!”
     *      若：新出价比原数据价格高，则：a. 给原数据档口创建转移支付单   b.新档口占据改行数据位置。
     *
     *      等到晚上10:00，所有档口购买完成。再通过定时任务（11:30），给每个类型，按照价格从高到低，进行排序。
     *
     *
     *      LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
     *          wrapper.eq(Product::getType, specificType) // 筛选出特定类型的记录
     *        .orderByAsc(Product::getPrice)      // 升序排序，NULL 会在最前面
     *        .last("LIMIT 1");                   // 取第一条（即 price 最低或为 null）
     *
     * @param createDTO 购买入参
     * @return Integer
     */
    @Override
    @Transactional

    // TODO 要加锁，必须要锁住，有可能多个档口同时购买同一个推广营销位，导致购买失败，锁必须要做好
    // TODO 要加锁，必须要锁住，有可能多个档口同时购买同一个推广营销位，导致购买失败，锁必须要做好
    // TODO 要加锁，必须要锁住，有可能多个档口同时购买同一个推广营销位，导致购买失败，锁必须要做好


    public Integer create(AdPlayStoreCreateDTO createDTO) {

        // 判断当前档口出价是否低于最低价格，若是，则提示:“已经有档口出价更高了噢，请重新出价!”


        // 要判断当前是多个档口购买一个推广位，没有排序。还是多个档口购买同一个推广位，需要排序。


        // 这里还有支付对接的情况！！ 如果遇到支付延迟怎么办？


        return null;
    }


    // TODO 新增档口广告购买时，需要加锁，一定要锁住
    // TODO 新增档口广告购买时，需要加锁，一定要锁住
    // TODO 新增档口广告购买时，需要加锁，一定要锁住

}
