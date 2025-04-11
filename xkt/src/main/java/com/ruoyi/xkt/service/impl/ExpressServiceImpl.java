package com.ruoyi.xkt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.Express;
import com.ruoyi.xkt.domain.ExpressFeeConfig;
import com.ruoyi.xkt.domain.ExpressRegion;
import com.ruoyi.xkt.domain.Store;
import com.ruoyi.xkt.dto.express.ExpressContactDTO;
import com.ruoyi.xkt.mapper.*;
import com.ruoyi.xkt.service.IExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-03 13:37
 */
@Service
public class ExpressServiceImpl implements IExpressService {

    @Value("${express.default.province:510000}")
    private String expressDefaultProvince;
    @Value("${express.default.city:510100}")
    private String expressDefaultCity;
    @Value("${express.default.county:510114}")
    private String expressDefaultCounty;

    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private ExpressFeeConfigMapper expressFeeConfigMapper;
    @Autowired
    private StoreOrderExpressTrackMapper storeOrderExpressTrackMapper;
    @Autowired
    private ExpressRegionMapper expressRegionMapper;
    @Autowired
    private StoreMapper storeMapper;

    @Override
    public void checkExpress(Long expressId) {
        Assert.notNull(expressId);
        Express express = expressMapper.selectById(expressId);
        Assert.isTrue(BeanValidators.exists(express), "快递不存在");
        Assert.isTrue(express.getSystemDeliverAccess(), "快递不可用");
    }

    @Override
    public Express getById(Long expressId) {
        return expressMapper.selectById(expressId);
    }

    @Override
    public ExpressFeeConfig getExpressFeeConfig(Long expressId, String provinceCode, String cityCode,
                                                String countyCode) {
        Assert.notNull(expressId);
        Assert.notEmpty(provinceCode);
        Assert.notEmpty(cityCode);
        Assert.notEmpty(countyCode);
        Map<String, ExpressFeeConfig> map = expressFeeConfigMapper.selectList(Wrappers.lambdaQuery(ExpressFeeConfig.class)
                .eq(ExpressFeeConfig::getExpressId, expressId)
                .in(ExpressFeeConfig::getRegionCode, Arrays.asList(provinceCode, cityCode, countyCode)))
                .stream()
                //过滤掉已被删除的配置
                .filter(BeanValidators::exists)
                .collect(Collectors.toMap(o -> o.getRegionCode(), o -> o, (n, o) -> n));
        ExpressFeeConfig expressFeeConfig = null;
        if (CollUtil.isNotEmpty(map)) {
            if (map.size() == 1) {
                expressFeeConfig = CollUtil.getFirst(map.values());
            } else {
                expressFeeConfig = map.get(countyCode);
                //按区市省从小到大去匹配
                if (expressFeeConfig == null) {
                    expressFeeConfig = map.get(cityCode);
                    if (expressFeeConfig == null) {
                        expressFeeConfig = map.get(provinceCode);
                    }
                }
            }
        }
        return expressFeeConfig;
    }

    @Override
    public ExpressContactDTO getStoreContact(Long storeId) {
        Assert.notNull(storeId);
        ExpressContactDTO expressContactDTO = new ExpressContactDTO();
        expressContactDTO.setProvinceCode(expressDefaultProvince);
        expressContactDTO.setCityCode(expressDefaultCity);
        expressContactDTO.setCountyCode(expressDefaultCounty);
        Store store = storeMapper.selectById(storeId);
        Assert.notNull(store);
        expressContactDTO.setDetailAddress(store.getStoreAddress());
        expressContactDTO.setContactName(store.getContactName());
        expressContactDTO.setContactPhoneNumber(store.getContactPhone());
        return expressContactDTO;
    }

    @Override
    public List<ExpressRegion> listRegionByCode(Collection<String> regionCodes) {
        if (CollUtil.isEmpty(regionCodes)) {
            return ListUtil.empty();
        }
        return expressRegionMapper.selectList(Wrappers.lambdaQuery(ExpressRegion.class)
                .in(ExpressRegion::getRegionCode, regionCodes));
    }
}
