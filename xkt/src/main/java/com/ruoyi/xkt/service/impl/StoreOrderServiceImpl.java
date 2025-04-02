package com.ruoyi.xkt.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.dto.order.StoreOrderAddDTO;
import com.ruoyi.xkt.dto.order.StoreOrderDetailInfoDTO;
import com.ruoyi.xkt.enums.EProductStatus;
import com.ruoyi.xkt.enums.EVoucherSequenceType;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IStoreOrderService;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-02 13:19
 */
@Service
public class StoreOrderServiceImpl implements IStoreOrderService {
    @Autowired
    private StoreOrderMapper storeOrderMapper;
    @Autowired
    private StoreOrderDetailMapper storeOrderDetailMapper;
    @Autowired
    private StoreOrderOperationRecordMapper storeOrderOperationRecordMapper;
    @Autowired
    private StoreOrderExpressTrackMapper storeOrderExpressTrackMapper;
    @Autowired
    private StoreProductMapper storeProductMapper;
    @Autowired
    private StoreProductColorSizeMapper storeProductColorSizeMapper;
    @Autowired
    private IVoucherSequenceService voucherSequenceService;

    @Transactional
    @Override
    public StoreOrderDetailInfoDTO createOrder(StoreOrderAddDTO storeOrderAddDTO) {
        checkOrderDetail(storeOrderAddDTO.getStoreId(), storeOrderAddDTO.getDetailList());
        //生成订单号
        String orderNo = voucherSequenceService.generateCode(storeOrderAddDTO.getStoreId(),
                EVoucherSequenceType.STORE_ORDER.getValue(), DateUtil.today());
        //TODO
        return null;
    }

    /**
     * 检查订单明细
     *
     * @param storeId
     * @param detailList
     */
    private void checkOrderDetail(Long storeId, List<StoreOrderAddDTO.OrderDetail> detailList) {
        Assert.notNull(storeId, "档口不能为空");
        Assert.notEmpty(detailList, "商品不能为空");
        Set<Long> spcsIds = detailList.stream()
                .map(StoreOrderAddDTO.OrderDetail::getStoreProdColorSizeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        //下单商品颜色尺码
        Map<Long, StoreProductColorSize> spcsMap = storeProductColorSizeMapper.selectByIds(spcsIds).stream()
                .collect(Collectors.toMap(StoreProductColorSize::getId, o -> o));
        List<Long> spIdList = spcsMap.values().stream()
                .map(StoreProductColorSize::getStoreProdId)
                .collect(Collectors.toList());
        //下单商品
        Map<Long, StoreProduct> spMap = storeProductMapper.selectByIds(spIdList).stream()
                .collect(Collectors.toMap(StoreProduct::getId, o -> o));
        for (StoreOrderAddDTO.OrderDetail detail : detailList) {
            Assert.notNull(detail.getStoreProdColorSizeId(), "商品颜色尺码异常");
            Integer goodsQuantity = detail.getGoodsQuantity();
            if (Objects.isNull(goodsQuantity) || goodsQuantity == 0) {
                throw new IllegalArgumentException("商品数量异常");
            }
            StoreProductColorSize spcs = spcsMap.get(detail.getStoreProdColorSizeId());
            Assert.isTrue(Objects.nonNull(spcs) && Constants.UNDELETED.equals(spcs.getDelFlag()),
                    "商品颜色尺码不存在");
            StoreProduct sp = spMap.get(spcs.getStoreProdId());
            Assert.isTrue(Objects.nonNull(sp) && Constants.UNDELETED.equals(sp.getDelFlag()),
                    "商品不存在");
            Assert.isTrue(storeId.equals(sp.getStoreId()), "系统不支持跨档口下单");
            Assert.isTrue(EProductStatus.accessOrder(sp.getProdStatus()), "商品状态异常");
        }
    }
}
