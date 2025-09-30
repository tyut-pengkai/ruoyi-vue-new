package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreColor;
import com.ruoyi.xkt.dto.storeColor.StoreColorDTO;
import com.ruoyi.xkt.mapper.StoreColorMapper;
import com.ruoyi.xkt.service.IStoreColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 档口所有颜色Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreColorServiceImpl implements IStoreColorService {

    final StoreColorMapper storeColorMapper;

    private static final Map<String, Integer> colorPriority = new HashMap<String, Integer>() {{
        put("白", 1); put("黑", 2); put("红", 3); put("棕", 4); put("灰", 5); put("卡", 6);
        put("银", 7); put("金", 8); put("蓝", 9); put("绿", 10); put("黄", 11); put("粉", 12);
        put("紫", 13); put("橙", 14); put("米", 15); put("杏", 16); put("香", 17); put("军", 18);
        put("酒", 19); put("咖", 20); put("焦", 21); put("墨", 22); put("豹", 23); put("蛇", 24);
    }};

    private static final  Map<String, Integer> suffixPriority = new HashMap<String, Integer>() {{
        put("", 1); put("单里", 2); put("绒里", 3); put("毛里", 4); put("标准筒", 5); put("加宽筒", 6);
        put("漆", 7); put("哑光", 8); put("亚光", 9); put("亮", 10); put("雕花", 11); put("爆裂纹", 12);
        put("马毛", 13); put("豹纹", 14); put("蛇纹", 15); put("石头纹", 16); put("羊翻", 17); put("牛翻", 18);
    }};


    /**
     * 查询档口所有颜色列表
     *
     * @param storeId 档口ID
     * @return List<StoreColorDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreColorDTO> list(Long storeId) {
        List<StoreColor> storeColorList = Optional.ofNullable(this.storeColorMapper.selectList(new LambdaQueryWrapper<StoreColor>()
                        .eq(StoreColor::getStoreId, storeId).eq(StoreColor::getDelFlag, Constants.UNDELETED)))
                .orElseThrow(() -> new ServiceException("当前档口没有颜色!", HttpStatus.ERROR));
        return storeColorList.stream()
                .map(x -> BeanUtil.toBean(x, StoreColorDTO.class).setStoreColorId(x.getId()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(StoreColorDTO::getColorName, dto -> dto, (existing, replacement) -> existing),
                        map -> new ArrayList<>(map.values())))
                .stream()
                .sorted(Comparator
                        .comparing((StoreColorDTO c) ->
                                colorPriority.getOrDefault(c.getColorName().substring(0, 1), Integer.MAX_VALUE))
                        .thenComparing(c ->
                                suffixPriority.entrySet().stream()
                                        .filter(e -> c.getColorName().contains(e.getKey()))
                                        .map(Map.Entry::getValue)
                                        .findFirst()
                                        .orElse(Integer.MAX_VALUE))
                        .thenComparing(StoreColorDTO::getOrderNum)
                )
                .collect(Collectors.toList());
    }


}
